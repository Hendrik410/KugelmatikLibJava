package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 30.08.2015.
 */
public class Home extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.Home;
    }

    @Override
    public int getDataSize() {
        return 4;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.putInt(BinaryHelper.FlipByteOrder(0xABCD));
    }
}
