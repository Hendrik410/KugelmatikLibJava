package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by Hendrik on 03.09.2015.
 * Interface für eine Choreograpy
 */
public interface IChoreography {
    /**
     * Gibt die Position einer Kugel zu einem Zeitpunkt einer Choreography zurück
     * @param x Die absolute x-Koordinate der Kugel
     * @param y Die absolute y-Koordiante der Kugel
     * @param millis Die Zeit seit dem Start der Choreography
     * @param choreographyManager Der ChoreographyManager der die Choreography abspielt
     * @return Die Höhe der Kugel
     */
    short GetHeight(int x, int y, long millis, ChoreographyManager choreographyManager);
}
