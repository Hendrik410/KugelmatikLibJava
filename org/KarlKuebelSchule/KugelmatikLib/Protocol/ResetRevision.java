package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Setzt die Revsionsnummer für Befehle auf dem Cluster zurück.
 */
public class ResetRevision extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.ResetRevision;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
