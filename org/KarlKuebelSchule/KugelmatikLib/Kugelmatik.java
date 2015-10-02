package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

import java.net.SocketException;

/**
 * Created by Hendrik on 29.08.2015.
 * Repräsentiert eine Kugelmatik
 */
public class Kugelmatik {

    private Cluster[] clusters;

    private Log log;

    public Kugelmatik(@NotNull IAddressProvider addressProvider, Log log) throws SocketException {
        this.log = log;
        clusters = new Cluster[Config.KugelmatikHeight * Config.KugelmatikWidth];
        for(int x = 0; x < Config.KugelmatikWidth; x++){
            for(int y = 0; y < Config.KugelmatikHeight; y++){
                clusters[y * Config.KugelmatikWidth + x] = new Cluster(this, addressProvider.GetAddress(x, y), x, y);
            }
        }
    }

    /**
     * Sendet ein Ping an alle Cluster
     */
    public void SendPing(){
        for(Cluster cluster : clusters){
            cluster.SendPing();
        }
    }

    /**
     * Lässt die grüne LED aller Cluster blinken
     */
    public void BlinkGreen(){
        for(Cluster cluster : clusters)
            cluster.BlinkGreen();
    }

    /**
     * Lässt die rote LED aller Cluster blinken
     */
    public void BlinkRed(){
        for(Cluster cluster : clusters){
            cluster.BlinkRed();
        }
    }

    /**
     * Sendet alle nicht bestätigten Packets neu
     * @return Gibt true zurück, wenn ein Packet gesendet wurde
     */
    public boolean ResendPackets(){
        boolean anyPacketsSend = false;
        for(Cluster cluster : clusters)
            anyPacketsSend |= cluster.ResendPackets();
        return anyPacketsSend;
    }

    /**
     * Setzt die Höhe aller Stepper auf eine Höhe
     * @param height Die Höhe auf die alle Stepper gesetzt werden sollen
     */
    public void MoveAllSteppers(short height){
        for(Cluster cluster : clusters)
            cluster.MoveAllSteppers(height);
    }

    /**
     * Sendet alle Höhenänderungen ohne Garantie
     * @return Gibt true zurück, wenn ein Packet gesendet wurde
     */
    public boolean SendMovementData(){
        return SendMovementData(false);
    }

    /**
     * Sendet alle Höhenänderungen and die Cluster
     * @param guaranteed Gibt an, ob eine Bestätigung gesendet werden soll
     * @return Gibt true zurück, wenn ein Packet gesendet wurde
     */
    public boolean SendMovementData(boolean guaranteed){
        boolean anyPacketsSend = false;
        for(Cluster cluster : clusters)
            anyPacketsSend |= cluster.SendMovementData(guaranteed);
        return anyPacketsSend;
    }

    /**
     * Gibt true zurück, wenn es nicht beantwortete Packets gibt
     */
    public boolean AnyPacketsPending(){
        for(Cluster cluster : clusters)
            if(cluster.AnyPacketsPending())
                return true;
        return false;
    }

    /**
     * Gibt ein Cluster nach seiner Position zurück
     * @param x Die x-Position des Clusters
     * @param y Die y-Position des Clusters
     * @return Das Cluster an der Position
     */
    public Cluster getClusterByPosition(byte x, byte y){
        return clusters[y * Config.KugelmatikWidth + x];
    }

    /**
     * Gibt einen Stepper nach seiner absoluten Position zurück
     * @param x Die x-Position des Steppers
     * @param y Die y-Position des Steppers
     * @return Der Stepper an der Position
     */
    public Stepper getStepperByPosition(int x, int y){
        byte cx = (byte)(x / Cluster.Width);
        byte cy = (byte)(y / Cluster.Height);
        byte sx = (byte)(x % Cluster.Width);
        byte sy = (byte)(y % Cluster.Height);
        return getClusterByPosition(cx, cy).getStepperByPosition(sx, sy);
    }

    /**
     * Gibt die Breite der Kugelmatik in Steppern zurück
     */
    public int getStepperWidth(){
        return Config.KugelmatikWidth * Cluster.Width;
    }

    /**
     * Gibt die Höhe der Kugelmatik in Steppern zurück
     */
    public int getStepperHeight(){
        return Config.KugelmatikHeight * Cluster.Height;
    }

    /**
     * Gibt einen Array mit allen Clustern zurück
     */
    public Cluster[] getAllCluster(){
        return clusters;
    }

    /**
     * Gibt das Log-Objekt der Kugelmatik zurück
     */
    public Log Log(){
        return log;
    }
}
