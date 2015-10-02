package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Created by Hendrik on 31.08.2015.
 * Befehl um den Status der Stepper abzurufen
 */
public class GetData extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.GetData;
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {

    }
}
