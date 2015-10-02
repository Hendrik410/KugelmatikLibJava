package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 31.08.2015.
 * Befehl um die Konfiguration vom Cluster abzurufen
 */
public class GetClusterConfig extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.Info;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
