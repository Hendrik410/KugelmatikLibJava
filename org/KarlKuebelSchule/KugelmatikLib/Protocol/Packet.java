package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Hendrik on 30.08.2015.
 * Eine abstrakte Klasse f�r Packete die an die Kugelmatik �bermittelt werden
 */
public abstract class Packet {
    public static final int HeadSize = 9;

    /**
     * Gibt den Typ des Packetes zur�ck
     * @return Der Typ des Packets
     */
    public abstract PacketType getType();

    /**
     * Gibt die Gr��e der Daten des Packets in bytes zur�ck
     * @return Die Gr��e der Daten des Packets
     */
    public abstract int getDataSize();

    /**
     * Erstellt ein Packet mit allen Informationen f�r den Befehl
     * @param guaranteed Soll das Packet garantiert ankommen
     * @param revision Die Revision das Paketes
     * @return Das DatagramPacket das via eines DatagramSockets an das Cluster �bermittelt wird
     */
    public DatagramPacket getPacket(boolean guaranteed, int revision){
        ByteBuffer buffer = ByteBuffer.allocate(HeadSize + getDataSize());
        //buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put((byte)'K');
        buffer.put((byte)'K');
        buffer.put((byte)'S');
        buffer.put(guaranteed ? (byte)1 : (byte)0);
        buffer.put(getType().getByteValue());
        buffer.putInt(BinaryHelper.FlipByteOrder(revision));
        allocateBuffer(buffer);
        return new DatagramPacket(buffer.array(), HeadSize + getDataSize());
    }

    /**
     * Wird von den Erben �berschrieben und schreibt die Daten in den buffer
     * @param buffer Der buffer in den die Daten geschrieben werden
     */
    protected abstract void allocateBuffer(ByteBuffer buffer);
}
