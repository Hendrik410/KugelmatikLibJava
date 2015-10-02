package org.KarlKuebelSchule.KugelmatikLib.Choreographies;

import org.KarlKuebelSchule.KugelmatikLib.ChoreographyManager;
import org.KarlKuebelSchule.KugelmatikLib.Config;
import org.KarlKuebelSchule.KugelmatikLib.IChoreography;

import java.security.InvalidParameterException;

/**
 * Created by Hendrik on 03.09.2015.
 * Stellt eine sich bewegende Sinuswelle da
 */
public class SineWave implements IChoreography {

    WaveDirection direction;
    float timeFactor;
    float frequency;

    /**
     * Erstellt eine neue SineWave-Instanz
     * @param direction Die Richtung in der Sinuswelle
     * @param timeFactor Der Zeitfaktor der Sinuswelle
     * @param frequency Die Frequenz der Sinuswelle
     */
    public SineWave(WaveDirection direction, float timeFactor, float frequency){
        if(frequency == 0)
            throw new InvalidParameterException("frequency is out of range");

        this.direction = direction;
        this.timeFactor = timeFactor;
        this.frequency = frequency;
    }

    @Override
    public short GetHeight(int x, int y, long millis, ChoreographyManager choreographyManager) {
        float v = x;
        if (direction == WaveDirection.Y)
            v = y;

        // Sinuswelle erstellen
        double sinWave = Math.sin((v + millis * timeFactor) * frequency);

        sinWave += 1; // in den Bereich [0, 2] verschieben
        sinWave /= 2; // normalisieren

        return (short)(sinWave * Config.MaxHeight);
    }

}
