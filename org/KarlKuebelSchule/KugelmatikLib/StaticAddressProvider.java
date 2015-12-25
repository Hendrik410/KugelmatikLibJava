package org.KarlKuebelSchule.KugelmatikLib;

import java.net.InetAddress;

/**
 * Der StaticAddressProvider gibt für jede Position doe selbe Ardesse zurück
 */
public class StaticAddressProvider implements IAddressProvider {
    private InetAddress address;

    /**
     * Erstellt eine neue StaticAddressProvider-Instanz
     * @param address Die Adresse die für alle Cluster zurückgegeben wird
     */
    public StaticAddressProvider(InetAddress address){
        if (address == null)
            throw new IllegalArgumentException("address is null");

        this.address = address;
    }

    @Override
    public InetAddress getAddress(int x, int y) {
        return address;
    }
}
