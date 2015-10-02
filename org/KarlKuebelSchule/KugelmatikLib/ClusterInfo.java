package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

/**
 * Created by Hendrik on 31.08.2015.
 * Enh�lt alle Informationen �ber das Cluster
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
     * Gibt zur�ck ob das Cluster einen Busy-Befehl ausf�hrt
     */
    public boolean isRunningBusyCommand() {
        return runningBusyCommand;
    }

    /**
     * Gibt die BuildVersion der Firmware zur�ck
     */
    public byte getBuildVersion() {
        return buildVersion;
    }

    /**
     * Gibt die Konfiguration des Clusters zur�ck
     */
    public ClusterConfig getConfig() {
        return config;
    }

    /**
     * Gibt die DelayTime des Clusters zur�ck
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * Gibt die h�chste empfangene Revision des Clusters zur�ck
     */
    public int getHighestRevision() {
        return highestRevision;
    }
}
