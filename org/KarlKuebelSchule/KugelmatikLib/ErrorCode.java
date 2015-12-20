package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by henrik.kunzelmann on 20.12.2015.
 */
public enum ErrorCode {
    None((byte)0),
    TooShort((byte)1),
    InvalidX((byte)2),
    InvalidY((byte)3),
    InvalidMagic((byte)4),
    BufferOverflow((byte)5),
    UnkownPacket((byte)6),
    NotRunningBusy((byte)7),
    InvalidConfigValue((byte)8),
    InvalidHeight((byte)9),
    InvalidValue((byte)10);


    private byte value;

    private ErrorCode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
