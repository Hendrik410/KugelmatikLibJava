package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Befehl um die Konfiguration vom Cluster abzurufen
 */
public class GetClusterInfo extends Packet {
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
