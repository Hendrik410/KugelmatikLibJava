package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

import java.security.InvalidParameterException;

/**
 * Created by Hendrik on 03.09.2015.
 * Der ChoreographyManager berechnet die Bewegungen für eine Chereographie und sendet die Daten an die Kugelmatik.
 */
public class ChoreographyManager implements Runnable {
    private Kugelmatik kugelmatik;
    private IChoreography choreography;
    private int targetFPS;
    private int fps;

    private Thread thread;

    private boolean stopRequested;
    private boolean choreographyRunning = false;

    /**
     * Erstellt eine neue ChoreographyManager-Instanz
     *
     * @param kugelmatik   Die Kugelmatik auf der die Choreography abgespielt werden soll
     * @param targetFPS    Die Zielframerate die erreicht werden soll
     * @param choreography Die Choreography die abgespielt werden soll
     */
    public ChoreographyManager(@NotNull Kugelmatik kugelmatik, int targetFPS, @NotNull IChoreography choreography) {
        if (targetFPS <= 0)
            throw new InvalidParameterException("targetFPS is out of range");

        this.choreography = choreography;
        this.kugelmatik = kugelmatik;
        this.targetFPS = targetFPS;
        thread = new Thread(this);
    }

    /**
     * Startet die Choreography
     */
    public void Start() {
        stopRequested = false;
        thread.start();
    }

    /**
     * Zeigt den ersten Frame der Choreography
     */
    public void StartOnlyFirstFrame() {
        stopRequested = true;
        thread.start();
    }

    /**
     * Hält die Choreography an
     */
    public void Stop() {
        stopRequested = true;
    }

    /**
     * Gibt zurück, ob die Choreography läuft
     */
    public boolean isChoreographyRunning() {
        return choreographyRunning;
    }

    /**
     * Gibt die aktuelle Framerate zurück
     */
    public int getFPS() {
        return fps;
    }

    private void setSteppers() {
        for (int x = 0; x < kugelmatik.getStepperWidth(); x++)
            for (int y = 0; y < kugelmatik.getStepperHeight(); y++)
                kugelmatik.getStepperByPosition(x, y).moveTo(choreography.getHeight(x, y, 0, this));
    }

    @Override
    public void run() {
        setSteppers();

        kugelmatik.sendMovementData(true);
        while (kugelmatik.isAnyPacketPending()) {
            sleep(500);
            kugelmatik.resendPackets();
        }

        sleep(5000);
        choreographyRunning = true;
        long startTime = System.currentTimeMillis();

        while (!stopRequested) {
            long frameStartTime = System.currentTimeMillis();
            long timeRunning = System.currentTimeMillis() - startTime;

            setSteppers();
            kugelmatik.sendMovementData();

            if (timeRunning % 2 == 0)
                kugelmatik.sendPing();

            int sleepTime = (int) (1000f / targetFPS) - (int) (System.currentTimeMillis() - frameStartTime); // berechnen wie lange der Thread schlafen soll um die TargetFPS zu erreichen
            if (sleepTime > 0)
                sleep(sleepTime);

            fps = (int) Math.ceil(1000f / (int) (System.currentTimeMillis() - frameStartTime));

        }
        choreographyRunning = false;
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}