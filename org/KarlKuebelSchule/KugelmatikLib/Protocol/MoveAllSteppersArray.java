package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;
import org.KarlKuebelSchule.KugelmatikLib.Cluster;
import org.KarlKuebelSchule.KugelmatikLib.Stepper;

import java.nio.ByteBuffer;

/**
 * Created by Henrik Kunzelmann on 20.12.2015.
 * Befehl zum Bewegen aller Stepper mit unterschiedlicher Hoehe und Waittime.
 */
public class MoveAllSteppersArray extends Packet {
    private short[] heights;
    private byte[] waitTimes;

    public MoveAllSteppersArray(short[] heights, byte[] waitTimes){
        if (heights == null)
            throw new IllegalArgumentException("heights is null");
        if (waitTimes == null)
            throw new IllegalArgumentException("waitTimes is null");
        if (heights.length != Cluster.Width * Cluster.Height)
            throw new IllegalArgumentException("heights length does not match stepper count of cluster");
        if (waitTimes.length != heights.length)
            throw new IllegalArgumentException("heights length does not match wait times lengths");

        this.heights = heights;
        this.waitTimes = waitTimes;
    }
    public MoveAllSteppersArray(Stepper[] steppers){
        if (steppers == null)
            throw new IllegalArgumentException("steppers is null");
        if (steppers.length != Cluster.Width * Cluster.Height)
            throw new IllegalArgumentException("steppers length does not match stepper count of cluster");

        this.heights = new short[steppers.length];
        this.waitTimes = new byte[steppers.length];

        for (int i = 0; i < steppers.length; i++) {
            Stepper stepper = steppers[i];
            this.heights[i] = stepper.getHeight();
            this.waitTimes[i] = stepper.getWaitTime();
        }
    }

    @Override
    public PacketType getType() {
        return PacketType.AllSteppersArray;
    }

    @Override
    public int getDataSize() {
        return heights.length * Short.BYTES + waitTimes.length * Byte.BYTES;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        for (int i = 0; i < heights.length; i++) {
            buffer.putShort(BinaryHelper.flipByteOrder(heights[i]));
            buffer.put(waitTimes[i]);
        }
    }
 }
