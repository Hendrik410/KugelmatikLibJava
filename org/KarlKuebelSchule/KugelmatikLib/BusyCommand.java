package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by henrik.kunzelmann on 20.12.2015.
 */
public enum BusyCommand {
    None((byte)0),
    Home((byte)1),
    Fix ((byte)2),
    HomeStepper((byte)3),
    Unkown(Byte.MAX_VALUE);

    private byte value;

    private BusyCommand(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
