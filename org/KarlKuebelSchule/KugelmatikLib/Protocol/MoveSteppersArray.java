package org.KarlKuebelSchule.KugelmatikLib.Protocol;

import org.KarlKuebelSchule.KugelmatikLib.BinaryHelper;
import org.KarlKuebelSchule.KugelmatikLib.Config;
import org.KarlKuebelSchule.KugelmatikLib.Stepper;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

/**
 * Created by Hendrik on 30.08.2015.
 * Befehl zum Bewegen mehrerer Kugeln in jeweils eigene H�hen mit eigener waitTime
 */
public class MoveSteppersArray extends Packet {

    private Item[] items;

    public MoveSteppersArray(Item[] items){
        if(items.length > Byte.MAX_VALUE || items.length == 0)
            throw new InvalidParameterException("items has invalid size (" + items.length + ")");

        this.items = items;
    }
    public MoveSteppersArray(Stepper[] steppers){
        if(steppers.length > Byte.MAX_VALUE || steppers.length == 0)
            throw new InvalidParameterException("items has invalid size (" + items.length + ")");

        this.items = new Item[steppers.length];
        for(int i = 0; i < steppers.length; i++) {
            this.items[i] = new Item(steppers[i]);
        }
    }

    @Override
    public PacketType getType() {
        return PacketType.SteppersArray;
    }

    @Override
    public int getDataSize() {
        return 1 + items.length * 4;
    }

    @Override
    protected void allocateBuffer(ByteBuffer buffer) {
        buffer.put(((byte)items.length));
        for(Item item : items){
            buffer.put(((byte)((item.getX() << 4) | item.getY())));
            buffer.putShort(BinaryHelper.FlipByteOrder(item.height));
            buffer.put(item.getWaitTime());
        }
    }

    public class Item {
        private byte x;
        private byte y;
        private short height;
        private byte waitTime;

        public Item(byte x, byte y, short height, byte WaitTime) {
            if(height > Config.MaxHeight)
                throw new InvalidParameterException("height is out of range");

            this.x = x;
            this.y = y;
            this.height = height;
            this.waitTime = WaitTime;
        }
        public Item(Stepper stepper){
            this.x = stepper.getX();
            this.y = stepper.getY();
            this.height = stepper.getHeight();
            this.waitTime = stepper.getWaitTime();
        }

        public byte getX() {
            return x;
        }

        public byte getY() {
            return y;
        }

        public byte getWaitTime() {
            return waitTime;
        }

        public short getHeight() {
            return height;
        }
    }

}
