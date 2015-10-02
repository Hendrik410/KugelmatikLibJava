package org.KarlKuebelSchule.KugelmatikLib;

import java.util.Arrays;

/**
 * Created by Hendrik on 31.08.2015.
 * Eine Hilfsklasse f�r Schrittmotoren
 */
public class StepperUtil {

    /**
     * �berpr�ft ob alle Stepper die selbe H�he haben
     * @param steppers Die zu �berpr�fenden Stepper
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
     * �berpr�ft ob alle Stepper die selbe waitTime haben
     * @param steppers Die zu �berpr�fenden Stepper
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
     * �berpr�ft ob alle Stepper die selbe H�he und waitTime haben
     * @param steppers Die zu �berpr�fenden Stepper
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
