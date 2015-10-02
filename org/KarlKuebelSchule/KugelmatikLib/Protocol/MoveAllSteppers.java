package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;
import org.KarlKuebelSchule.KugelmatikLib.Config;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

/**
 * Created by Hendrik on 30.08.2015.
 * Befehl um alle Stepper des Clusters auf eine Höhe zu bringen
 */
public class MoveAllSteppers extends Packet {

    short height;
    byte waitTime;

    public MoveAllSteppers(short height, byte waitTime){
        if(height > Config.MaxHeight)
            throw new InvalidParameterException("height is out of range");

        this.height = height;
        this.waitTime = waitTime;
    }

    @Override
    public PacketType getType() {
        return PacketType.AllSteppers;
    }

    @Override
    public int getDataSize() {
        return 3;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.putShort(BinaryHelper.FlipByteOrder(height));
        buffer.put(waitTime);
    }
}
