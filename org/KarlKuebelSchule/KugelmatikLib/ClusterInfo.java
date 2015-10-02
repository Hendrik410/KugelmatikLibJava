package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

/**
 * Created by Hendrik on 31.08.2015.
 * Enhält alle Informationen über das Cluster
 */
public class ClusterInfo {
    private byte buildVersion;
    private boolean runningBusyCommand;
    private int delayTime;
    private int highestRevision;
    private ClusterConfig config;

    public ClusterInfo(byte buildVersion, boolean runningBusyCommand, int delayTime, int highestRevision, @NotNull ClusterConfig config){
        this.buildVersion = buildVersion;
        this.runningBusyCommand = runningBusyCommand;
        this.delayTime = delayTime;
        this.highestRevision = highestRevision;
        this.config = config;
    }

    /**
     * Gibt zurück ob das Cluster einen Busy-Befehl ausführt
     */
    public boolean isRunningBusyCommand() {
        return runningBusyCommand;
    }

    /**
     * Gibt die BuildVersion der Firmware zurück
     */
    public byte getBuildVersion() {
        return buildVersion;
    }

    /**
     * Gibt die Konfiguration des Clusters zurück
     */
    public ClusterConfig getConfig() {
        return config;
    }

    /**
     * Gibt die DelayTime des Clusters zurück
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * Gibt die höchste empfangene Revision des Clusters zurück
     */
    public int getHighestRevision() {
        return highestRevision;
    }
}
