package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

/**
 * Created by Hendrik on 31.08.2015.
 * Enh�lt alle Informationen �ber das Cluster
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
     * Gibt die BuildVersion der Firmware zur�ck
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
     * Gibt die h�chste empfangene Revision des Clusters zur�ck
     */
    public int getHighestRevision() {
        return highestRevision;
    }

    /**
     * Gibt die Konfiguration des Clusters zur�ck
     */
    public ClusterConfig getConfig() {
        return config;
    }

    /**
     * Gibt den ErrorCode zurueck, welcher am Cluster zuletzt aufgetreten ist
     */
    public ErrorCode getLastErrorCode() {
        return lastErrorCode;
    }

    /**
     * Gibt einen Wert in Bytes zurück, welcher den freien Arbeitsspeicher auf dem Cluster angibt
     */
    public int getFreeRam() {
        return freeRam;
    }
}
