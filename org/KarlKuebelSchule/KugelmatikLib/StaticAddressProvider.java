package org.KarlKuebelSchule.KugelmatikLib;

import java.net.InetAddress;

/**
 * Created by Hendrik on 31.08.2015.
 * Der StaticAddressProvider gibt f�r jede Position doe selbe Ardesse zur�ck
 */
public class StaticAddressProvider implements IAddressProvider {

    private InetAddress address;

    /**
     * Erstellt eine neue StaticAddressProvider-Instanz
     * @param address Die Adresse die f�r alle Cluster zur�ckgegeben wird
     */
    public StaticAddressProvider(InetAddress address){
        this.address = address;
    }

    @Override
    public InetAddress GetAddress(int x, int y) {
        return address;
    }
}
