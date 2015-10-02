package org.KarlKuebelSchule.KugelmatikLib;

import java.net.InetAddress;

/**
 * Created by Hendrik on 29.08.2015.
 * Stellt eine Schnittstelle f�r die R�ckgabe der IP-Adressen der Cluster dar
 */
public interface IAddressProvider {

    /**
     * Gibt die InetAddress eines Clusters zur�ck
     * @param x Die x-Koordinate des Clusters
     * @param y Die y-Koordinate des Clusters
     * @return Die InetAddresse �ber die das Cluster zu erreichen ist
     */
    InetAddress GetAddress(int x, int y);
}
