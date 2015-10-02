package org.KarlKuebelSchule.KugelmatikLib;


/**
 * Created by Hendrik on 31.08.2015.
 * Eine Hilfsklasse für Binäroperationen
 */
public class BinaryHelper {

   public static short ShortFromByteArray(byte[] bytes, int offset) {
        return ((short)((bytes[offset]) << 8 | (bytes[offset + 1] & 0xFF)));
    }

    public static int IntFromByteArray(byte[] bytes, int offset) {
        return bytes[offset] << 24 | (bytes[offset + 1] & 0xFF) << 16 | (bytes[offset + 2] & 0xFF) << 8 | (bytes[offset + 3] & 0xFF);
    }

    public static long LongFromByteArray(byte[] bytes, int offset){
        return (bytes[offset] & 0xFF) << 54 | (bytes[offset + 1] & 0xFF) << 48 | (bytes[offset + 2] & 0xFF) << 40 | (bytes[offset + 3] & 0xFF) << 32 | (bytes[offset + 4] & 0xFF) << 24 | (bytes[offset + 5] & 0xFF) << 16 | (bytes[offset + 6] & 0xFF) << 8 | (bytes[offset + 7] & 0xFF);
    }

    public static int FlipByteOrder(int val){
        byte[] bytes = {
                ((byte)val),
                (byte)(val >> 8),
                (byte)(val >> 16),
                (byte)(val >> 24),
        };
        return IntFromByteArray(bytes, 0);
    }

    public static short FlipByteOrder(short val){
        return (short)((val & 0xFF) << 8 | (val) >> 8);
    }

    public static long FlipByteOrder(long val){
        byte[] bytes = {
                ((byte)val),
                (byte)(val >> 8),
                (byte)(val >> 16),
                (byte)(val >> 24),
                (byte)(val >> 32),
                (byte)(val >> 40),
                (byte)(val >> 48),
                (byte)(val >> 56),
        };
        return LongFromByteArray(bytes, 0);
    }

}
