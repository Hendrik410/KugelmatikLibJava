package org.KarlKuebelSchule.KugelmatikLib.Protocol;

/**
 * Created by Hendrik on 30.08.2015.
 * Eine Enumeration aller Möglichen Pakettypen
 */
public enum PacketType{
    Ping ((byte)1),
    Ack ((byte)2),

    Stepper ((byte)3),
    Steppers ((byte)4),
    SteppersArray ((byte)5),
    SteppersRectangle ((byte)6),
    SteppersRectangleArray ((byte)7),
    AllSteppers ((byte)8),
    AllSteppersArray ((byte)9),

    Home ((byte)10),

    ResetRevision ((byte)11),
    Fix ((byte)12),

    HomeStepper ((byte)13),

    GetData ((byte)14),
    Info ((byte)15),
    Config ((byte)16),

    BlinkGreen ((byte)17),
    BlinkRed ((byte)18),

    Stop ((byte)19);
    
    private byte numVal;

    PacketType(byte numVal){
        this.numVal = numVal;
    }

    /**
     * Gibt den byte-Wert des Eintrags zurück.
     * @return Der byte-Wert des Eintrags
     */
    public byte getByteValue(){
        return numVal;
    }


    /**
     * Gibt zurück ob das Cluster auf den Befehl antwortet.
     */
    public boolean DoesClusterAnswer(){
        switch(this)
        {
            case Ping:
            case Info:
            case GetData:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gibt zurück ob der Befehl ein busy-Befehl ist
     */
    public boolean IsBusyCommand(){
        switch(this)
        {
            case Home:
            case Fix:
            case HomeStepper:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gibt zurück ob der Befehl während eines busy-Befehls gesendet werden kann
     */
    public boolean CanSendWhileBusy(){
        switch(this)
        {
            case Ping:
            case Info:
            case Stop:
                return true;
            default:
                return false;
        }
    }
}
