package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 30.08.2015.
 * Setzt die Revsionsnummer f�r Befehle auf dem Cluster zur�ck.
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
