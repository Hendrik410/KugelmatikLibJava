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
import java.util.Map;

/**
 * Created by Hendrik on 29.08.2015.
 * Repräsentiert ein Cluster der Kugelmatik
 */
public class Cluster {
    public static final byte Width = 5;
    public static final byte Height = 6;

    private Kugelmatik kugelmatik;
    private int x;
    private int y;

    private DatagramSocket socket;
    private DatagramIncomeListener incomeListener;

    private IPingChangedEventHandler pingChangedEventHandler;

    private ClusterInfo clusterInfo;
    private Stepper[] steppers;

    private int currentRevision = 0;
    private long lastSuccessfulPingTime = -1;
    private int ping = -1;

    private Map<Integer, Packet> packetsToAcknowledge;
    private Map<Integer, Long> packetsSentTimes;

    /**
     * Gibt eine neue Instanz eines Clusters zurück
     *
     * @param kugelmatik Die kugelmatik zu der
     * @param address    Die InetAdresse des Clusters
     * @param x          Die x-Koordinate der Position des Clusters in der kugelmatik
     * @param y          Die y-Koordinate der Position der Clusters in der kugelmatik
     */
    public Cluster(@NotNull Kugelmatik kugelmatik, InetAddress address, int x, int y) {
        if (x < 0)
            throw new IllegalArgumentException("The argument 'x' is out of range.");
        if (y < 0)
            throw new IllegalArgumentException("The argument 'y' is out of range.");


        packetsToAcknowledge = new HashMap<>();
        packetsSentTimes = new HashMap<>();

        this.kugelmatik = kugelmatik;
        this.x = x;
        this.y = y;

        steppers = new Stepper[Width * Height];

        // die Reihenfolge der beiden for-Schleifen darf sich nicht ändern
        // da die Firmware genau diese Reihenfolge der Stepper erwartet
        for (byte sX = 0; sX < Width; sX++)
            for (byte sY = 0; sY < Height; sY++)
                steppers[sY * Width + sX] = new Stepper(this, sX, sY);

        if (address != null) {
            try {
                socket = new DatagramSocket();
                socket.connect(address, Config.ProtocolPort);
                incomeListener = new DatagramIncomeListener(this, socket, "listen_" + x + "_" + y);
                incomeListener.listen();
            } catch (IOException e) {
                this.kugelmatik.getLog().error("Error while creating socket for cluster [%s] with ip %s", getID(), address.getHostAddress());
                e.printStackTrace();
            }

            sendPing();
        }
    }

    /**
     * Wird aufgerufen, wenn eine Verbindung hergestellt wurde.
     */
    private void onConnected() {
        kugelmatik.getLog().debug("Cluster [%s, address = %s] onConnected()", getID(), socket.getInetAddress().toString());
        resetRevision();
        sendGetData();
        sendGetClusterConfig();
    }

    /**
     * Gibt einen Wert zurück, der angibt, ob sich ein Stepper geändert hat.
     */
    public boolean hasStepperChanged() {
        for (Stepper stepper : steppers)
            if (stepper.hasDataChanged())
                return true;
        return false;
    }

    /**
     * Sendet alle noch austehende Packets deren Sendezeit mehr als MinimumResendTimeout zurück liegt
     *
     * @return True wenn Packets gesendet wurden
     */
    public boolean resendPackets() {
        boolean anyPacketsSend = false;

        for (Map.Entry<Integer, Packet> entry : packetsToAcknowledge.entrySet())
            if (System.currentTimeMillis() - packetsSentTimes.get(entry.getKey()) >= Config.MinimumResendTimeout)
                anyPacketsSend |= sendPacketInternal(entry.getValue(), true, entry.getKey());
        return anyPacketsSend;
    }

    /**
     * Sende ein Packet an das Cluster (ohne Garantie).
     *
     * @param packet Das Packet das gesendet werden soll
     */
    public boolean sendPacket(Packet packet) {
        return sendPacket(packet, false);
    }

    /**
     * Sende ein Packet an das Cluster, wahlweise mit Garantie.
     *
     * @param packet     Das Packet das gesendet werden soll
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob ein Packet gesendet wurde
     */
    public boolean sendPacket(@NotNull Packet packet, boolean guaranteed) {
        return sendPacketInternal(packet, guaranteed, currentRevision++);
    }

    /**
     * Interne Methode zum senden eines Packets
     *
     * @param packet     Das Packet das gesendet werden soll
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @param revision   Die Revision mit der das Packet gesendet werden soll
     * @return Gibt zurück ob ein Packet gesendet wurde
     */
    protected boolean sendPacketInternal(@NotNull Packet packet, boolean guaranteed, int revision) {
        // bei keiner Verbindung Paket ignorieren
        if (socket == null)
            return false;

        if (ping < 0) {
            if (Config.IgnoreGuaranteedWhenOffline)
                guaranteed = false;

            // Ping erlauben, sonst kann die Software nicht feststellen ob das Cluster verfügbar ist
            if (!packet.getType().equals(PacketType.Ping) && Config.IgnorePacketWhenOffline)
                return false;
        }


        if (packet.getType().doesClusterAnswer())
            guaranteed = false;

        if (guaranteed) {
            packetsToAcknowledge.put(currentRevision, packet);
            packetsSentTimes.put(currentRevision, System.currentTimeMillis());
        }

        DatagramPacket datagramPacket = packet.getPacket(guaranteed, currentRevision);
        try {
            kugelmatik.getLog().verbose("%s: Sent %s with rev %d", getID(), packet.getClass().getSimpleName(), revision);
            socket.send(datagramPacket);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Bewegt alle Kugeln auf eine Höhe.
     *
     * @param height Die Höhe zu der sich die Kugeln bewegen sollen
     */
    public void moveAllSteppers(short height) {
        if (height < 0 || height > Config.MaxHeight)
            throw new IllegalArgumentException("height is out of range");

        for (Stepper stepper : steppers)
            stepper.moveTo(height);
    }

    /**
     * Sendet die Höhenänderungen an das Cluster ohne Garantie
     *
     * @return Gibt zurück ob Packets gesendet wurden
     */
    public boolean sendMovementData() {
        return sendMovementData(false);
    }

    public boolean sendMovementData(boolean guaranteed) {
        return sendMovementData(guaranteed, false);
    }

    /**
     * Sendet die Höhenänderungen an das Cluster
     *
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob Packets gesendet wurden
     */
    public boolean sendMovementData(boolean guaranteed, boolean sendAllSteppers) {
        boolean sentSomething = sendMovementDataInternal(guaranteed, sendAllSteppers);
        if (sentSomething) {
            for (Stepper stepper : steppers)
                stepper.updated();
        }
        return sentSomething;
    }

    /**
     * Interne Methode zum Senden der Höhenänderungen an das Cluster
     *
     * @param guaranteed Bei true mit Garantie, bei false ohne Garantie
     * @return Gibt zurück ob Packets gesendet wurden
     */
    protected boolean sendMovementDataInternal(boolean guaranteed, boolean sendAllSteppers) {
        if (!hasStepperChanged() && !sendAllSteppers)
            return false;

        Stepper[] changedSteppers;

        if (sendAllSteppers)
            changedSteppers = steppers;
        else
            changedSteppers = Arrays.asList(steppers).stream().filter(Stepper::hasDataChanged).toArray(Stepper[]::new);

        if (changedSteppers.length == 0)
            return false;

        if (changedSteppers.length == 1) {
            Stepper stepper = changedSteppers[0];
            return sendPacket(new MoveStepper(stepper.getX(), stepper.getY(), stepper.getHeight(), stepper.getWaitTime()), guaranteed);
        }

        boolean allSteppersSameValues = StepperUtil.AllSteppersSameValues(steppers);

        if (allSteppersSameValues)
            return sendPacket(new MoveAllSteppers(steppers[0].getHeight(), steppers[0].getWaitTime()), guaranteed);

        // TODO detect rectangles

        // wenn die Anzahl der Stepper zu groß ist, dann
        // ist es uneffizent die Position für jeden Stepper mitzuschicken
        if (changedSteppers.length < 8) {
            boolean allChangedSameValues = StepperUtil.AllSteppersSameValues(changedSteppers);

            if (allChangedSameValues)
                return sendPacket(new MoveSteppers(changedSteppers, changedSteppers[0].getHeight(), changedSteppers[0].getWaitTime()), guaranteed);
            else
                return sendPacket(new MoveSteppersArray(changedSteppers), guaranteed);
        }

        return sendPacket(new MoveAllSteppersArray(steppers), guaranteed);
    }

    /**
     * Setzt die Revisionszählung des Clusters zurück.
     */
    public void resetRevision() {
        sendPacket(new ResetRevision());
        currentRevision = 0;
    }

    /**
     * Lässt die grüne LED blinken.
     */
    public void blinkGreen() {
        sendPacket(new BlinkGreen());
    }

    /**
     * Lässt die rote LED blinken.
     */
    public void blinkRed() {
        sendPacket(new BlinkRed());
    }

    /**
     * Sendet ein Ping-Packet an das Cluster. Die Rundlaufzeit kann mit getPing() abgerufen werden.
     */
    public void sendPing() {
        if (lastSuccessfulPingTime < 0 || System.currentTimeMillis() - lastSuccessfulPingTime > 5000)
            setPing(-1);

        sendPacket(new Ping(System.currentTimeMillis()));
    }

    /**
     * Sendet eine Stop-Befehl an das Cluster
     */
    public void sendStop() {
        sendPacket(new Stop(), true);
    }

    /**
     * Setze alle Kugeln des Clusters auf Home
     */
    public void sendHome() {
        sendPacket(new Home(), true);

        for (Stepper stepper : steppers)
            stepper.setHeight(((short) 0));
    }

    /**
     * Setzte eine Kugel des Klusters auf Home.
     *
     * @param x Die x-Koordinate der Kugel
     * @param y Die y-Koordinate der Kugel
     */
    public void sendHome(byte x, byte y) {
        Stepper stepper = getStepperByPosition(x, y);
        stepper.reset();
        sendPacket(new HomeStepper(x, y), true);
    }

    /**
     * Wickelt eine Kugel ab und wieder auf.
     *
     * @param x Die x-Koordinate der Kugel
     * @param y Die y-Koordinate der Kugel
     */
    public boolean sendFix(byte x, byte y) {
        Stepper stepper = getStepperByPosition(x, y);
        stepper.reset();
        return sendPacket(new Fix(x, y), true);
    }

    /**
     * Ruft den Status der Stepper vom Cluster ab
     */
    public void sendGetData() {
        sendPacket(new GetData());
    }

    /**
     * Ruft die Konfiguration des Clusters ab
     */
    public void sendGetClusterConfig() {
        sendPacket(new GetClusterInfo());
    }

    /**
     * Wird vom DatagramIncomeListener aufgerufen wenn ein neues Packet angekommen ist
     *
     * @param packet Das angekommene Packet das verarbeitet werden soll
     */
    public void onReceive(DatagramPacket packet) {
        if (packet.getLength() == 0)
            return;

        byte[] data = packet.getData();

        if (data.length < Packet.HeadSize || data[0] != 'K' || data[1] != 'K' || data[2] != 'S')
            return;

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(data));
        try {
            input.skip(4);

            PacketType type = PacketType.values()[input.read() - 1];
            int revision = BinaryHelper.flipByteOrder(input.readInt());
            acknowledge(revision);

            String verbose = "Got packet | Length: " + data.length + " | Revision: " + revision + " | ";
            switch (type) {
                case Ping:
                    verbose += "Ping";
                    if (data.length - Packet.HeadSize != Long.BYTES)
                        break;

                    if (getPing() < 0)
                        onConnected();

                    lastSuccessfulPingTime = System.currentTimeMillis();

                    long sendTime = input.readLong();
                    setPing((int) (System.currentTimeMillis() - sendTime));
                    break;
                case Ack:
                    verbose += "Ack";
                    break;
                case Info:
                    verbose += "Config";
                    byte buildVersion = input.readByte();

                    BusyCommand currentBusyCommand = BusyCommand.None;
                    if (buildVersion >= 11)
                        currentBusyCommand = BusyCommand.values()[input.readByte()];
                    else if (buildVersion >= 8)
                        currentBusyCommand = input.read() > 0 ? BusyCommand.Unkown : BusyCommand.None;

                    int highestRevision = 0;
                    if (buildVersion >= 9)
                        highestRevision = BinaryHelper.flipByteOrder(input.readInt());

                    byte stepMode = (byte) input.read();

                    int delayTime = BinaryHelper.flipByteOrder(input.readInt());

                    boolean useBreak = false;
                    if (buildVersion >= 6)
                        useBreak = input.read() > 0;

                    ErrorCode lastErrorCode = ErrorCode.None;
                    if (buildVersion >= 12)
                        lastErrorCode = ErrorCode.values()[input.readByte()];

                    int freeRam = -1;
                    if (buildVersion >= 14)
                        freeRam = input.readInt();

                    clusterInfo = new ClusterInfo(buildVersion, currentBusyCommand, highestRevision, new ClusterConfig(StepMode.values()[stepMode - 1], delayTime, useBreak), lastErrorCode, freeRam);
                    break;
                case GetData:
                    verbose += "StepperData";
                    for (byte x = 0; x < Width; x++) // for-Schleife muss mit Firmware übereinstimmen
                        for (byte y = 0; y < Height; y++) {
                            Stepper stepper = getStepperByPosition(x, y);


                            short height = BinaryHelper.flipByteOrder(input.readShort());
                            if (height > Config.MaxHeight)
                                continue; // Höhe ignorieren

                            byte waitTime = input.readByte();
                            stepper.setHeight(height);
                            stepper.setWaitTime(waitTime);
                        }
                    break;

            }
            kugelmatik.getLog().verbose(getID() + ": " + verbose);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acknowledge(int rev) {
        packetsToAcknowledge.remove(rev);
        packetsSentTimes.remove(rev);
    }

    /**
     * Setzt den PingChangedEventHandler
     *
     * @param eventHandler Der PingChangedEventHandler
     */
    public void setPingChangedEventHandler(IPingChangedEventHandler eventHandler) {
        pingChangedEventHandler = eventHandler;
    }

    /**
     * Setzt den Ping-Wert und ruft bei Änderungen das entsprechende Event auf.
     *
     * @param ping Der neue Ping-Wert
     */
    private void setPing(int ping) {
        if (this.ping != ping) {
            this.ping = ping;

            if (pingChangedEventHandler != null)
                pingChangedEventHandler.onPingChanged(this);
        }
    }

    /**
     * Ruft die Rundlaufzeit für das Cluster ab.
     */
    public int getPing() {
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
    public boolean isAnyPacketPending() {
        return packetsToAcknowledge.size() != 0;
    }

    /**
     * Ruft die Anzahl der nicht beantworteten Pakete ab
     */
    public int getPendingPacketsCount() {
        return packetsToAcknowledge.size();
    }

    /**
     * Gibt den Stepper an der entsprechenden Postion zurück
     *
     * @param x Die x-Koordinate des Steppers
     * @param y Die y-Koordinate des Steppers
     * @return Der Stepper an der Postion
     */
    public Stepper getStepperByPosition(byte x, byte y) {
        if (x < 0 || x >= Width)
            throw new IllegalArgumentException("x is out of range");
        if (y < 0 || y >= Height)
            throw new IllegalArgumentException("y is out of range");

        return steppers[y * Width + x];
    }

    /**
     * Gibt einen Stepper anhand seines Indexes zurück
     *
     * @param index Der Index des Steppers
     * @return Der Stepper an der Stelle index
     */
    public Stepper getStepperByIndex(int index) {
        if (index >= steppers.length)
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
    public String getID() {
        return String.format("cluster_%d_%d", x + 1, y + 1);
    }
}