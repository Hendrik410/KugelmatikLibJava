package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Hendrik on 30.08.2015.
 * Eine abstrakte Klasse für Packete die an die Kugelmatik übermittelt werden
 */
public abstract class Packet {
    /**
     * Gibt die Größe des Paket-Headers in Bytes an.
     */
    public static final int HeadSize = 9;

    /**
     * Gibt den Typ des Packets zurück.
     * @return Der Typ des Packets
     */
    public abstract PacketType getType();

    /**
     * Gibt die Größe der Daten des Packets in Bytes zurück.
     * @return Die Größe der Daten des Packets in Bytes.
     */
    public abstract int getDataSize();

    /**
     * Erstellt ein Packet mit allen Informationen für den Befehl.
     * @param guaranteed Gibt an, ob das Packet garantiert ankommen soll.
     * @param revision Gibt die Revision des Pakcets an.
     * @return Das DatagramPacket das via eines DatagramSockets an das Cluster übermittelt wird
     */
    public DatagramPacket getPacket(boolean guaranteed, int revision){
        ByteBuffer buffer = ByteBuffer.allocate(HeadSize + getDataSize());
        //buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.put((byte)'K');
        buffer.put((byte)'K');
        buffer.put((byte)'S');
        buffer.put(guaranteed ? (byte)1 : (byte)0);
        buffer.put(getType().getByteValue());
        buffer.putInt(BinaryHelper.flipByteOrder(revision));

        allocateBuffer(buffer);
        return new DatagramPacket(buffer.array(), HeadSize + getDataSize());
    }

    /**
     * Schreibt die Daten des Packets in einen ByteBuffer.
     * @param buffer Der ByteBuffer in dem die Daten geschrieben werden sollen.
     */
    protected abstract void allocateBuffer(ByteBuffer buffer);
}
