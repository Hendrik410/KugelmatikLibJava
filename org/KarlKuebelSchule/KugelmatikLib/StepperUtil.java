package org.KarlKuebelSchule.KugelmatikLib;

import java.util.Arrays;

/**
 * Created by Hendrik on 31.08.2015.
 * Eine Hilfsklasse für Schrittmotoren
 */
public class StepperUtil {

    /**
     * Überprüft ob alle Stepper die selbe Höhe haben
     * @param steppers Die zu überprüfenden Stepper
     * @return True wenn gleich, False wenn ungleich
     */
    public static boolean AllSteppersSameHeight(Stepper[] steppers){
        if(steppers.length <= 1)
            return true;

        final int height = steppers[0].getHeight();
        return Arrays.asList(steppers).stream()
                .filter(stepper -> stepper.getHeight() == height).count() == steppers.length;
    }

    /**
     * Überprüft ob alle Stepper die selbe waitTime haben
     * @param steppers Die zu überprüfenden Stepper
     * @return True wenn gleich, False wenn ungleich
     */
    public static boolean AllSteppersSameWaitTime(Stepper[] steppers){
        if(steppers.length <= 1)
            return true;

        final byte waitTime = steppers[0].getWaitTime();
        return Arrays.asList(steppers).stream()
                .filter(stepper -> stepper.getWaitTime() == waitTime).count() == steppers.length;
    }

    /**
     * Überprüft ob alle Stepper die selbe Höhe und waitTime haben
     * @param steppers Die zu überprüfenden Stepper
     * @return True wenn gleich, False wenn ungleich
     */
    public static boolean AllSteppersSameValues(Stepper[] steppers){
        if(steppers.length <= 1)
            return true;

        final int height = steppers[0].getHeight();
        final byte waitTime = steppers[0].getWaitTime();

        return Arrays.asList(steppers).stream()
                        .filter(stepper -> stepper.getHeight() == height && stepper.getWaitTime() == waitTime).count() == steppers.length;
    }
}
