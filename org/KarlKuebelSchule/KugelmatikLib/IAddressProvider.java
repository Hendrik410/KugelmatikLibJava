package org.KarlKuebelSchule.KugelmatikLib;

import java.net.InetAddress;

/**
 * Stellt eine Schnittstelle für die Rückgabe der IP-Adressen der Cluster dar
 */
public interface IAddressProvider {
    /**
     * Gibt die InetAddress eines Clusters zurück
     * @param x Die x-Koordinate des Clusters
     * @param y Die y-Koordinate des Clusters
     * @return Die InetAddress über die das Cluster zu erreichen ist
     */
    InetAddress getAddress(int x, int y);
}
