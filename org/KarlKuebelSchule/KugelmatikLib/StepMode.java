package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Stellt den Schrittmodus eines Clusters da
 */
public enum StepMode {
    HalfStep((byte)1),
    FullStep((byte)2),
    Both((byte)3);

    private byte numVal;

    StepMode(byte numVal){
        this.numVal = numVal;
    }

    /**
     * Gibt den byte-Wert des Eintrags zurück.
     * @return Der byte-Wert des Eintrags
     */
    public byte getByteValue(){
        return numVal;
    }
}