package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Gibt alle Befehle an bei dennen das Cluster in einem blockierenden (Busy) Zustand ist.
 */
public enum BusyCommand {
    None((byte)0),
    Home((byte)1),
    Fix ((byte)2),
    HomeStepper((byte)3),
    Unkown(Byte.MAX_VALUE);

    private byte value;

    BusyCommand(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
