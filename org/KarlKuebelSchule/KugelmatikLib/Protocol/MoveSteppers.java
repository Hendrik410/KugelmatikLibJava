package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.Config;
import org.KarlKuebelSchule.KugelmatikLib.Stepper;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

/**
 * Created by Hendrik on 31.08.2015.
 * Befehl um mehrere Kugeln auf eine Hoehe zu bringen
 */
public class MoveSteppers extends Packet {
    private Item[] items;
    private short height;
    private byte waitTime;

    public MoveSteppers(Item[] items, short height, byte waitTime) {
        if (items == null)
            throw new IllegalArgumentException("items is null");
        if (items.length == 0)
            throw new IllegalArgumentException("items is empty");
        if (height < 0 || height > Config.MaxHeight)
            throw new IllegalArgumentException("height is out of range");
        if (waitTime < 0)
            throw new IllegalArgumentException("waitTime is out of range");

        this.items = items;
        this.height = height;
        this.waitTime = waitTime;
    }

    public MoveSteppers(Stepper[] steppers, short height, byte waitTime) {
        if (steppers == null)
            throw new IllegalArgumentException("steppers is null");
        if (steppers.length == 0)
            throw new IllegalArgumentException("steppers is empty");
        if (height < 0 || height > Config.MaxHeight)
            throw new IllegalArgumentException("height is out of range");
        if (waitTime < 0)
            throw new IllegalArgumentException("waitTime is out of range");

        this.height = height;
        this.waitTime = waitTime;

        this.items = new Item[steppers.length];
        for (int i = 0; i < steppers.length; i++) {
            this.items[i] = new Item(steppers[i]);
        }
    }

    @Override
    public PacketType getType() {
        return PacketType.Steppers;
    }

    @Override
    public int getDataSize() {
        return 4 + items.length;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.put((byte) items.length);
        buffer.putShort(height);
        buffer.put(waitTime);
        for (int i = 0; i < items.length; i++) {
            buffer.put((byte) ((items[i].getX() << 4) | items[i].getY()));
        }
    }

    public class Item {
        private byte x, y;

        public Item(byte x, byte y) {
            this.x = x;
            this.y = y;
        }

        public Item(Stepper stepper) {
            this.x = stepper.getX();
            this.y = stepper.getY();
        }

        public byte getY() {
            return y;
        }

        public byte getX() {
            return x;
        }
    }
}
