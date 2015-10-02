package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by Hendrik on 30.08.2015.
 * Das Interface f�r das PingChangedEvent
 */
public interface IPingChangedEventHandler {

    /**
     * Wird aufgerufen wenn sich der Ping-Wert �ndert.
     * @param cluster Das Cluster bei dem die �nderung aufgetreten ist
     */
    void OnPingChanged(Cluster cluster);
}
