package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Befehl um die grüne LED am Cluster blinken zu lassen
 */
public class BlinkGreen extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.BlinkGreen;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
