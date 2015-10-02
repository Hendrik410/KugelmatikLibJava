package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;
import org.KarlKuebelSchule.KugelmatikLib.Protocol.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hendrik on 29.08.2015.
 * Repr�sentiert ein Cluster der Kugelmatik
 */
public class Cluster {
    public static final byte Width = 5;
    public static final byte Height = 6;

    private Kugelmatik Kugelmatik;
    private int x;
    private int y;

    private DatagramSocket socket;
    private DatagramIncomeListener incomeListener;

    private IPingChangedEventHandler pingChangedEventHandler;

    private ClusterInfo clusterInfo;
    private Stepper[] steppers;

    private int currentRevision = 0;
    private long lastPingTime = System.currentTimeMillis();
    private int ping = -1;
    private boolean dataChanged;

    private Map<Integer,Packet> packetsToAcknowledge;
    private Map<Integer, Long> packetsSentTimes;

    /**
     * Gibt eine neue Instanz eines Clusters zur�ck
     * @param kugelmatik Die Kugelmatik zu der
     * @param address Die InetAdresse des Clusters
     * @param x Die x-Koordinate der Position des Clusters in der Kugelmatik
     * @param y Die y-Koordinate der Position der Clusters in der Kugelmatik
     */
    public Cluster(@NotNull Kugelmatik kugelmatik, @NotNull InetAddress address, int x, int y){
        if(x < 0)
            throw new IllegalArgumentException("The argument 'x' is out of range.");
        if(y < 0)
            throw new IllegalArgumentException("The argument 'y' is out of range.");


        packetsToAcknowledge = new HashMap<>();
        packetsSentTimes = new HashMap<>();

        Kugelmatik = kugelmatik;
        this.x = x;
        this.y = y;

        steppers = new Stepper[Width * Height];
        for(byte sX = 0; sX < Width; sX++){
            for(byte sY = 0; sY < Height; sY++){
                steppers[sY * Width + sX] = new Stepper(this, sX, sY);
            }
        }

        try {
            socket = new DatagramSocket();
            socket.connect(address, Config.ProtocolPort);
            incomeListener = new DatagramIncomeListener(this, socket, "listen" + x + y);
            incomeListener.Listen();
        } catch (IOException e) {
            Kugelmatik.Log().Err(e);
            System.exit(-1);
        }

        ResetRevision();
        SendPing();
        SendGetData();
        SendGetClusterConfig();
    }

    /**
     * Wird aufgerufen wenn bei einem der Schtittmotoren eine Höhenänderung auftritt
     */
    public void ChildHasChanged(){
        dataChanged = true;
    }

    /**
     * Sendet alle noch austehende Packets deren Sendezeit mehr als MinimumResendTimeout zurück liegt
     * @return True wenn Packets gesendet wurden
     */
    public boolean ResendPackets(){
        boolean anyPacketsSend = false;
        Iterator<Map.Entry<Integer, Packet>> iterator = packetsToAcknowledge.entrySet().iterator();
        for(Map.Entry<Integer, Packet> entry : packetsToAcknowledge.entrySet()){
            if(System.currentTimeMillis() - packetsSentTimes.get(entry.getKey()) >= Config.MinimumResendTimeout){
                anyPacketsSend |= SendPacketInternal(entry.getValue(), true, entry.getKey());
            }
        }
        return anyPacketsSend;
    }

    /**
     * Sende ein Packet an das Cluster (ohne Garantie).
     * @param packet Das Packet das gesendet werden soll
     */
    public boolean SendPacket(Packet packet){
        return SendPacket(packet, false);
    }

    /**
     * Sende ein Packet an das Cluster, wahlweise mit Garantie.
     * @param packet Das Packet das gesendet werden soll
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob ein Packet gesendet wurde
     */
    public boolean SendPacket(@NotNull Packet packet, boolean guaranteed){
        return SendPacketInternal(packet, guaranteed, currentRevision++);
    }

    /**
     * Interne Methode zum senden eines Packets
     * @param packet Das Packet das gesendet werden soll
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @param revision Die Revision mit der das Packet gesendet werden soll
     * @return Gibt zurück ob ein Packet gesendet wurde
     */
    protected boolean SendPacketInternal(@NotNull Packet packet, boolean guaranteed, int revision){

        if(ping < 0){
            if(Config.IgnoreGuaranteedWhenOffline)
                guaranteed = false;
            if(Config.IgnorePacketWhenOffline)
                return false;
        }


        if(packet.getType().DoesClusterAnswer())
            guaranteed = false;

        if(guaranteed){
            packetsToAcknowledge.put(currentRevision, packet);
            packetsSentTimes.put(currentRevision, System.currentTimeMillis());
        }

        DatagramPacket datagramPacket = packet.getPacket(guaranteed, currentRevision);
        try {
            Kugelmatik.Log().Verbose(String.format(getID() + ": Sent %s with rev %d", packet.getClass().getSimpleName(), revision));
            socket.send(datagramPacket);

            return true;
        } catch (IOException e) {
            Kugelmatik.Log().Err(e);
            return false;
        }
    }

    /**
     * Bewegt alle Kugeln auf eine H�he.
     * @param height Die H�he zu der sich die Kugeln bewegen sollen
     */
    public void MoveAllSteppers(short height){
        if(height > Config.MaxHeight)
            throw new IllegalArgumentException("height is out of range");

        for(Stepper stepper : steppers)
            stepper.MoveTo(height);
    }

    /**
     * Sendet die Höhenänderungen an das Cluster ohne Garantie
     * @return Gibt zurück ob Packets gesendet wurden
     */
    public boolean SendMovementData(){
       return SendMovementData(false);
    }

    /**
     * Sendet die Höhenänderungen an das Cluster
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob Packets gesendet wurden
     */
    public boolean SendMovementData(boolean guaranteed){
        boolean sentSomething = SendMovementDataInternal(guaranteed);
        if(sentSomething){
            for(Stepper stepper : steppers)
                stepper.Updated();
        }
        return sentSomething;
    }

    /**
     * Interne Methode zum Senden der Höhenänderungen an das Cluster
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob Packets gesendet wurden
     */
    protected boolean SendMovementDataInternal(boolean guaranteed){
        if(!dataChanged)
            return false;

        Stepper[] changedSteppers =
                Arrays.asList(steppers).stream().
                        filter(Stepper::hasDataChanged).toArray(Stepper[]::new);

        if(changedSteppers.length == 0)
            return false;

        if(changedSteppers.length == 1){
            Stepper stepper = changedSteppers[0];
            return SendPacket(new MoveStepper(stepper.getX(), stepper.getY(), stepper.getHeight(), stepper.getWaitTime()), guaranteed);
        }

        boolean allSteppersSameValues = StepperUtil.AllSteppersSameValues(steppers);

        if(allSteppersSameValues)
            return SendPacket(new MoveAllSteppers(steppers[0].getHeight(), steppers[0].getWaitTime()), guaranteed);

        boolean allChangedSameValues = StepperUtil.AllSteppersSameValues(changedSteppers);

        if(allChangedSameValues){
            //TODO detect rectangles
            return SendPacket(new MoveSteppers(changedSteppers, changedSteppers[0].getHeight(), changedSteppers[0].getWaitTime()), guaranteed);
        }else{
            return SendPacket(new MoveStepperArray(changedSteppers), guaranteed);
        }
    }

    /**
     * Setzt die Revisionsz�hlung des Clusters zur�ck.
     */
    public void ResetRevision(){
        SendPacket(new ResetRevision());
        currentRevision = 0;
    }

    /**
     * Lässt die Grüne LED blinken
     */
    public void BlinkGreen(){
        SendPacket(new BlinkGreen());
    }

    /**
     * Lässt die Rote LED blinken
     */
    public void BlinkRed(){
        SendPacket(new BlinkRed());
    }

    /**
     * Sendet ein Ping-Packet an das Cluster. Die Rundlaufzeit kann mit getPing() abgerufen werden.
     */
    public void SendPing(){
        if(System.currentTimeMillis() - lastPingTime > 5000)
            setPing(-1);

        SendPacket(new Ping(System.currentTimeMillis()));
    }

    /**
     * Sendet eine Stop-Befehl an das Cluster
     */
    public void SendStop(){
        SendPacket(new Stop(), true);
    }

    /**
     * Setze alle Kugeln des Clusters auf Home
     */
    public void SendHome(){
        SendPacket(new Home(), true);

        for (Stepper stepper : steppers) {
            stepper.setHeight(((short)0));
        }
    }

    /**
     * Setzte eine Kugel des Klusters auf Home.
     * @param x Die x-Koordinate der Kugel
     * @param y Die y-Koordinate der Kugel
     */
    public void SendHome(byte x, byte y){
        if(x >= Width)
            throw new IllegalArgumentException("x is out of range");
        if(y >= Height)
            throw new IllegalArgumentException("y is out of range");

        SendPacket(new HomeStepper(x, y), true);
        steppers[y * Width + x].Reset();
    }

    /**
     * Wickelt eine Kugel ab und wieder auf.
     * @param x Die x-Koordinate der Kugel
     * @param y Die y-Koordinate der Kugel
     */
    public boolean SendFix(byte x, byte y){
        if(x >= Width)
            throw new IllegalArgumentException("x is out of range");
        if(y >= Height)
            throw new IllegalArgumentException("y is out of range");

        steppers[y * Width + x].Reset();
        return SendPacket(new Fix(x, y), true);
    }

    /**
     * Ruft den Status der Stepper vom Cluster ab
     */
    public void SendGetData(){
        SendPacket(new GetData());
    }

    /**
     * Ruft die Konfiguration des Clusters ab
     */
    public void SendGetClusterConfig() {
        SendPacket(new GetClusterConfig());
    }

    /**
     * Wird vom DatagramIncomeListener aufgerufen wenn ein neues Packet angekommen ist
     * @param packet Das angekommene Packet das verarbeitet werden soll
     */
    public void OnReceive(DatagramPacket packet){
        if(packet.getLength() == 0)
            return;

        byte[] data = packet.getData();

        if(data.length < Packet.HeadSize || data[0] != 'K' || data[1] != 'K' || data[2] != 'S')
            return;

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(data));
        try {
            input.skip(4);

            PacketType type = PacketType.values()[input.read() - 1];
            int revision = BinaryHelper.FlipByteOrder(input.readInt());
            String verbose = "Got packet | Length: " + data.length + " | Revision: " + revision + " | ";
            switch(type) {
                case Ping:
                    verbose += "Ping";
                    if(data.length - Packet.HeadSize == Long.SIZE / Byte.SIZE) {
                        long sendTime = input.readLong();
                        setPing((int)(System.currentTimeMillis() - sendTime));
                        Acknowledge(revision);
                    }
                    break;
                case Ack:
                    verbose += "Ack";
                    Acknowledge(revision);
                    break;
                case Info:
                    verbose += "Config";
                    byte buildVersion = (byte)input.read();

                    boolean isRunningBusyCommand = false;
                    if(buildVersion >= 8) {
                        isRunningBusyCommand = input.read() > 0;
                    }

                    int highestRevision = 0;
                    if(buildVersion >= 9) {
                        highestRevision = BinaryHelper.FlipByteOrder(input.readInt());
                    }

                    byte stepMode = (byte)input.read();

                    int delayTime = BinaryHelper.FlipByteOrder(input.readInt());

                    boolean useBreak = false;
                    if(buildVersion >= 6) {
                        useBreak = input.read() > 0;
                    }

                    clusterInfo = new ClusterInfo(buildVersion, isRunningBusyCommand, delayTime, highestRevision, new ClusterConfig(StepMode.values()[stepMode - 1], delayTime, useBreak));
                    Acknowledge(revision);
                    break;
                case GetData:
                    verbose += "StepperData";
                    for(byte x = 0; x < Width; x++) // for-Schleife muss mit Firmware übereinstimmen
                        for(byte y = 0; y < Height; y++) {
                            Stepper stepper = getStepperByPosition(x, y);


                            short height = BinaryHelper.FlipByteOrder(input.readShort());
                            if(height > Config.MaxHeight)
                                continue; // Höhe ignorieren

                            byte waitTime = input.readByte();
                            stepper.setHeight(height);
                            stepper.setWaitTime(waitTime);
                        }
                    Acknowledge(revision);
                    break;

            }
            Kugelmatik.Log().Verbose(getID() + ": " + verbose);

        } catch(IOException e) {
            Kugelmatik.Log().Err(e);
        }
    }

    private void Acknowledge(int rev){
        packetsToAcknowledge.remove(rev);
        packetsSentTimes.remove(rev);
    }

    /**
     * Setzt den PingChangedEventHandler
     * @param eventHandler Der PingChangedEventHandler
     */
    public void setPingChangedEventHandler(IPingChangedEventHandler eventHandler){
        pingChangedEventHandler = eventHandler;
    }

    /**
     * Setzt den Ping-Wert und ruft bei �nderungen das entsprechende Event auf.
     * @param ping Der neue Ping-Wert
     */
    private void setPing(int ping){
        if(this.ping != ping){
            lastPingTime = System.currentTimeMillis();
            this.ping = ping;
            if(pingChangedEventHandler != null)
                pingChangedEventHandler.OnPingChanged(this);
        }
    }

    /**
     * Ruft die Rundlaufzeit f�r das Cluster ab.
     */
    public int getPing(){
        return ping;
    }

    /**
     * Gibt die ClusterInfo zurück
     */
    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    /**
     * Ruft ab ob es nicht beantwortete Pakete gibt
     */
    public boolean AnyPacketsPending(){
        return packetsToAcknowledge.size() != 0;
    }

    /**
     * Ruft die Anzahl der nicht beantworteten Pakete ab
     */
    public int PendingPacketsCount(){
        return packetsToAcknowledge.size();
    }

    /**
     * Gibt den Stepper an der entsprechenden Postion zurück
     * @param x Die x-Koordinate des Steppers
     * @param y Die y-Koordinate des Steppers
     * @return Der Stepper an der Postion
     */
    public Stepper getStepperByPosition(byte x, byte y){
        if(y * Width + x > steppers.length)
            throw new InvalidParameterException("x | y is out of range");

        return steppers[y * Width + x];
    }

    /**
     * Gibt einen Stepper anhand seines Indexes zurück
     * @param index Der Index des Steppers
     * @return Der Stepper an der Stelle index
     */
    public Stepper getStepperByIndex(int index){
        if(index >= steppers.length)
            throw new InvalidParameterException("index is out of range");

        return steppers[index];
    }

    /**
     * Gibt die X-Koordinate des Clusters zurück
     */
    public int getX() {
        return x;
    }

    /**
     * Gibt die Y-Koordinate des Clusters zurück
     */
    public int getY() {
        return y;
    }

    /**
     * Gibt eine ID anhand der Position zurück
     */
    public String getID(){
        return (x+1) + "" + (y+1);
    }
}
