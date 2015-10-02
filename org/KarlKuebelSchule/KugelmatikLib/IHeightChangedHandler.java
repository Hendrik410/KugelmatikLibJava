package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Created by Hendrik on 30.08.2015.
 * Ein Interface f�r das HeightChangedEvent
 */
public interface IHeightChangedHandler {

    /**
     * Wird aufgerufen wenn sich die H�he des Schrittmotors �ndert
     * @param stepper Der Schrittmitor bei welchem die �nderung aufgetreten ist
     */
    void OnHeightChanged(Stepper stepper);
}
