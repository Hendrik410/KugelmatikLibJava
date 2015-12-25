package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by Hendrik on 30.08.2015.
 * Ein Interface für das HeightChangedEvent
 */
public interface IHeightChangedHandler {
    /**
     * Wird aufgerufen wenn sich die Höhe des Schrittmotors ändert
     * @param stepper Der Stepper bei dem eine Änderung aufgetreten ist
     */
    void onHeightChanged(Stepper stepper);
}
