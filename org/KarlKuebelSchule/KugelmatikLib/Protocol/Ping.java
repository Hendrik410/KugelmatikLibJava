package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 30.08.2015.
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
