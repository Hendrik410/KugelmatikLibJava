package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 31.08.2015.
 * Befehl um den aktuellen Busy-Befehl des Clusters zu stoppen oder, wenn kein Busy-Befehl läuft, die Bewegung der Kugeln zu stoppen.
 */
public class Stop extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.Stop;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
