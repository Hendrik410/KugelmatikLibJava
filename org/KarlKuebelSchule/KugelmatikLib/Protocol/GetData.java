package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import java.nio.ByteBuffer;

/**
 * Befehl um die Daten (Stepper Höhen und WaitTimes) von einem Cluster abzufragen.
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
