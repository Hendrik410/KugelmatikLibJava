package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import com.sun.istack.internal.NotNull;
import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;
import org.KarlKuebelSchule.KugelmatikLib.ClusterConfig;

import java.nio.ByteBuffer;

/**
 * Befehl um die Konfiguration an ein Cluster zu senden
 */
public class SendClusterConfig extends Packet {
    private ClusterConfig config;

    public SendClusterConfig(@NotNull ClusterConfig config){
        this.config = config;
    }

    @Override
    public PacketType getType() {
        return PacketType.Config;
    }

    @Override
    public int getDataSize() {
        return 6;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.put(config.getStepMode().getByteValue());
        buffer.putInt(BinaryHelper.flipByteOrder(config.getTickTime()));
        buffer.put((byte)(config.getUseBreak() ? 1 : 0));
    }
}
