package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.Cluster;
import org.KarlKuebelSchule.KugelmatikLib.Config;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 30.08.2015.
 * Befehl zum Bewegen eines Steppers
 */
public class MoveStepper extends Packet {

    private byte x, y;
    private int height;
    private byte waitTime;

    public MoveStepper(byte x, byte y, int height, byte waitTime){
        if(x >= Cluster.Width)
            throw new IllegalArgumentException("x is out of range");
        if(y >= Cluster.Height)
            throw new IllegalArgumentException("y is out of range");
        if(height > Config.MaxHeight || height < 0)
            throw new IllegalArgumentException("height is out of range");

        this.x = x;
        this.y = y;
        this.height = height;
        this.waitTime = waitTime;
    }

    @Override
    public PacketType getType() {
        return PacketType.Stepper;
    }

    @Override
    public int getDataSize() {
        return 4;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.put((byte)((x << 4) | y));
        buffer.putChar((char)height);
        buffer.put(waitTime);
    }
}
