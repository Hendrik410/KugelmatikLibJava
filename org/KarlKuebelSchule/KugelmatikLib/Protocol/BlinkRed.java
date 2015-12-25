package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Befehl um die rote LED am Cluster blinken zu lassen
 */
public class BlinkRed extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.BlinkRed;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
