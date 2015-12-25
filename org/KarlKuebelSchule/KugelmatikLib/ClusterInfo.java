package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

/**
 * Enhält alle Informationen über das Cluster
 */
public class ClusterInfo {
    private byte buildVersion;
    private BusyCommand currentBusyCommand;
    private int highestRevision;
    private ClusterConfig config;
    private ErrorCode lastErrorCode;
    private int freeRam;

    public ClusterInfo(byte buildVersion, BusyCommand currentBusyCommand, int highestRevision, @NotNull ClusterConfig config, ErrorCode lastErrorCode, int freeFram){
        this.buildVersion = buildVersion;
        this.currentBusyCommand = currentBusyCommand;
        this.highestRevision = highestRevision;
        this.config = config;
        this.lastErrorCode = lastErrorCode;
        this.freeRam = freeFram;
    }

    /**
     * Gibt die BuildVersion der Firmware zurück
     */
    public byte getBuildVersion() {
        return buildVersion;
    }

    /**
     * Gibt das BusyCommand zurueck, welcher am Cluster ausgefuehrt wird
     */
    public BusyCommand getCurrentBusyCommand() {
        return currentBusyCommand;
    }

    /**
     * Gibt die höchste empfangene Revision des Clusters zurück
     */
    public int getHighestRevision() {
        return highestRevision;
    }

    /**
     * Gibt die Konfiguration des Clusters zurück
     */
    public ClusterConfig getConfig() {
        return config;
    }

    /**
     * Gibt den ErrorCode zurueck, welcher am Cluster zuletzt aufgetreten i.st
     */
    public ErrorCode getLastErrorCode() {
        return lastErrorCode;
    }

    /**
     * Gibt einen Wert in Bytes zurück, wie viel freier Arbeitsspeicher auf dem Cluster verfügbar ist.
     */
    public int getFreeRam() {
        return freeRam;
    }
}
