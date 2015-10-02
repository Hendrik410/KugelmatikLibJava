package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by Hendrik on 30.08.2015.
 * Das Interface für das PingChangedEvent
 */
public interface IPingChangedEventHandler {

    /**
     * Wird aufgerufen wenn sich der Ping-Wert ändert.
     * @param cluster Das Cluster bei dem die Änderung aufgetreten ist
     */
    void OnPingChanged(Cluster cluster);
}
