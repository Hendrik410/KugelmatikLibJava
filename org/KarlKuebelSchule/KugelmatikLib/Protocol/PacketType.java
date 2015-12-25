package org.KarlKuebelSchule.KugelmatikLib.Protocol;

/**
 * Eine Enumeration aller M�glichen Pakettypen
 */
public enum PacketType {
    Ping((byte) 1),
    Ack((byte) 2),

    Stepper((byte) 3),
    Steppers((byte) 4),
    SteppersArray((byte) 5),
    SteppersRectangle((byte) 6),
    SteppersRectangleArray((byte) 7),
    AllSteppers((byte) 8),
    AllSteppersArray((byte) 9),

    Home((byte) 10),

    ResetRevision((byte) 11),
    Fix((byte) 12),

    HomeStepper((byte) 13),

    GetData((byte) 14),
    Info((byte) 15),
    Config((byte) 16),

    BlinkGreen((byte) 17),
    BlinkRed((byte) 18),

    Stop((byte) 19);

    private byte numVal;

    PacketType(byte numVal) {
        this.numVal = numVal;
    }

    /**
     * Gibt den Byte-Wert des Eintrags zurück.
     *
     * @return Der Byte-Wert des Eintrags
     */
    public byte getByteValue() {
        return numVal;
    }


    /**
     * Gibt einen Wert zurück, der angibt, ob das Cluster auf den Befehl antwortet.
     */
    public boolean doesClusterAnswer() {
        switch (this) {
            case Ping:
            case Info:
            case GetData:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gibt einen Wert zurück, der angibt, ob der Befehl ein Busy-Befehl ist
     */
    public boolean isBusyCommand() {
        switch (this) {
            case Home:
            case Fix:
            case HomeStepper:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gibt einen Wert zurück, der angibt, ob der Befehl während eines Busy-Befehls gesendet werden kann
     */
    public boolean canSendWhileBusy() {
        switch (this) {
            case Ping:
            case Info:
            case Stop:
                return true;
            default:
                return false;
        }
    }
}
