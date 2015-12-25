package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Paket zum Berechnen der Rundlaufzeit.
 */
public class Ping extends Packet {
    private long time;

    public Ping(long time){
        this.time = time;
    }

    @Override
    public PacketType getType() {
        return PacketType.Ping;
    }

    @Override
    public int getDataSize() {
        return 8;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.putLong(time);
    }
}
