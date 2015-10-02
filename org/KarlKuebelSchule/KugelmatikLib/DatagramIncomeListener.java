package org.KarlKuebelSchule.KugelmatikLib;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Hendrik on 30.08.2015.
 * Der Thread wartet auf ein ankommendes Packet an dem DatagramSocket
 */
public class DatagramIncomeListener implements Runnable {

    private Thread thread;
    private String name;

    private DatagramSocket socket;
    private Cluster cluster;

    /**
     * Erstellt eine neue DatagramIncomeListener-Instanz
     * @param cluster Das Cluster das bei einem neuen Packet informiert werden soll
     * @param socket Der Socket auf dem gehorcht werden soll
     * @param name Der Name des Listeners (Entscheident für den Thread)
     */
    public DatagramIncomeListener(Cluster cluster, DatagramSocket socket, String name){
        this.name = name;
        this.socket = socket;
        this.cluster = cluster;
    }

    /**
     * Beginnt auf ankommende Packets zu warten
     */
    public void Listen(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }

    @Override
    public void run() {
        while(true){
            byte[] buf = new byte[Config.ReceiveBufferLength];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
            packet.setData(data);
            cluster.OnReceive(packet);
        }
    }
}
