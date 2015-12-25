package org.KarlKuebelSchule.KugelmatikLib;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Hendrik on 29.08.2015.
 * Der StandardAddressProvider gibt die Adresse eines Clusters anhand seiner Position zurück
 */
public class StandardAddressProvider implements IAddressProvider {
    @Override
    public InetAddress getAddress(int x, int y) {
        byte lanId = (byte) ((y + 1) * 10 + (x + 1));
        try {
            return Inet4Address.getByAddress(new byte[]{(byte) 192, (byte) 168, 88, lanId});
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
