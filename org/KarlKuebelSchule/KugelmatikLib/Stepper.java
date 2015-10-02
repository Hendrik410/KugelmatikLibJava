package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

/**
 * Created by Hendrik on 29.08.2015.
 * Repr�sentiert einen Schrittmotor eines Clusters
 */
public class Stepper {

    private byte x;
    private byte y;

    private int absoluteX;
    private int absoluteY;

    private Cluster cluster;

    private short height = 0;
    private byte waitTime = 0;
    private boolean dataChanged = false;

    private IHeightChangedHandler heightChangedHandler;

    public Stepper(@NotNull Cluster cluster, byte x, byte y){
        if(x < 0 || x >= Cluster.Width)
            throw new IllegalArgumentException("x");
        if(y < 0 || y >= Cluster.Height)
            throw new IllegalArgumentException("y");

        this.cluster = cluster;
        this.x = x;
        this.y = y;

        absoluteX = cluster.getX() * Cluster.Width + x;
        absoluteY = cluster.getY() * Cluster.Height + y;
    }

    /**
     * Setzt die Werte die Kugel zur�ck, zum Beispiel nach einem Home-Befehl
     */
    public void Reset(){
        setHeight((short)0);
        dataChanged = false;
    }

    /**
     * Bewegt die Kugel in eine bestimmte H�he
     * @param height Die H�he der Kugel
     */
    public synchronized void MoveTo(short height){
        MoveTo(height, Config.DefaultWaitTime);
    }

    /**
     * Bewegt die Kugel in eine bestimmte H�he zu einer bestimmten Zeit
     * @param height Die H�he der Kugel
     * @param waitTime Die Zeit die gewartet werden soll
     */
    public synchronized void MoveTo(short height, byte waitTime){
        if(height > Config.MaxHeight)
            throw new IllegalArgumentException("height is out of range");

        if(this.height == height && this.waitTime == waitTime)
            return;

        setHeight(height);
        this.waitTime = waitTime;
        dataChanged = true;
        cluster.ChildHasChanged();
    }

    /**
     * Setzte die Kugel auf Home.
     */
    public void SendHome(){
        cluster.SendHome(x, y);
        Reset();
    }

    /**
     * Wickelt die Kugel auf und wieder ab.
     */
    public void SendFix(){
        cluster.SendFix(x, y);
        Reset();
    }

    /**
     * Setzt den HeightChangedEventHandler
     * @param heightChangedHandler Der HeightChangedEventHandler
     */
    public void setHeightChangedHandler(IHeightChangedHandler heightChangedHandler) {
        this.heightChangedHandler = heightChangedHandler;
    }

    /**
     * Gibt die WaitTime zur�ck.
     */
    public byte getWaitTime() {
        return waitTime;
    }

    /**
     * Setzt die waitTime der Kugel, sendet jedoch keinen Befehl an das Cluster.
     * @param waitTime Die waitTime der Kugel
     */
    public void setWaitTime(byte waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * Gibt zur�ck ob sich die H�he der Kugel seit dem letzten Senden ver�ndert hat.
     */
    public boolean hasDataChanged() {
        return dataChanged;
    }

    /**
     * Legt fest, das die �nderungen an das Cluster gesendet wurden.
     */
    public void Updated(){
        dataChanged = false;
    }

    /**
     * Gibt die H�he der Kugel zur�ck.
     */
    public short getHeight() {
        return height;
    }

    /**
     * Setzt die H�he der Kugel, sendet jedoch keinen Befehl an das Cluster.
     * @param height Die H�he der Kugel
     */
    public void setHeight(short height) {
        if(this.height != height){
            this.height = height;
            if(heightChangedHandler != null)
                heightChangedHandler.OnHeightChanged(this);
        }
    }

    /**
     * Gibt die x-Koordinate des Motors in einem Cluster zur�ck
     */
    public byte getX(){
        return x;
    }

    /**
     * Gibt die y-Koordinate des Motors in einem Cluster zur�ck.
     */
    public byte getY(){
        return y;
    }

    /**
     * Gibt die absolute x-Koordinate der Postion des Steppers zur�ck
     */
    public int getAbsoluteX(){
        return absoluteX;
    }

    /**
     * Gibt die absolute y-Koordinate der Postion des Steppers zur�ck
     */
    public int getAbsoluteY() {
        return absoluteY;
    }

    /**
     * Gibt das zugeh�rige Cluster zur�ck.
     */
    public Cluster getCluster(){
        return cluster;
    }
}
