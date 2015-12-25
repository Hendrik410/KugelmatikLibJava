package org.KarlKuebelSchule.KugelmatikLib.Choreographies;

import org.KarlKuebelSchule.KugelmatikLib.ChoreographyManager;
import org.KarlKuebelSchule.KugelmatikLib.Config;
import org.KarlKuebelSchule.KugelmatikLib.IChoreography;

/**
 * Stellt eine sich bewegende Sinuswelle da
 */
public class SineWave implements IChoreography {
    private WaveDirection direction;
    private float timeFactor;
    private float frequency;

    /**
     * Erstellt eine neue SineWave-Instanz
     * @param direction Die Richtung in der Sinuswelle
     * @param timeFactor Der Zeitfaktor der Sinuswelle
     * @param frequency Die Frequenz der Sinuswelle
     */
    public SineWave(WaveDirection direction, float timeFactor, float frequency){
        if(frequency == 0)
            throw new IllegalArgumentException("frequency is out of range");

        this.direction = direction;
        this.timeFactor = timeFactor;
        this.frequency = frequency;
    }

    @Override
    public int getHeight(int x, int y, long millis, ChoreographyManager choreographyManager) {
        float v = x;
        if (direction == WaveDirection.Y)
            v = y;

        // Sinuswelle berechnen
        double sinWave = Math.sin((v + millis * timeFactor) * frequency);

        sinWave += 1; // in den Bereich [0, 2] verschieben
        sinWave /= 2; // normalisieren

        return (int)(sinWave * Config.MaxHeight);
    }

}
