import org.KarlKuebelSchule.KugelmatikLib.*;

import java.net.InetAddress;

/**
 * Created by henrik.kunzelmann on 25.12.2015.
 */
public class Program {
    public static void main(String[] args) {
        Log log = new Log(LogLevel.Verbose);
        try {
            log.info("KugelmatikTest running...");
            log.info("=========================");

            InetAddress clusterAddress = InetAddress.getByName("192.168.178.24");
            log.info("Connecting to %s", clusterAddress.toString());

            // Config setzen
            Config.KugelmatikWidth = 1;
            Config.KugelmatikHeight = 1;

            Kugelmatik kugelmatik = new Kugelmatik(new StaticAddressProvider(clusterAddress), log);
            Cluster cluster = kugelmatik.getClusterByPosition(0, 0);

            log.info("Waiting for connection...");
            while (!cluster.isConnected()) {
                cluster.sendPing();
                sleep(500);
            }
            log.info("Got connection, ping: %dms", cluster.getPing());

            sleep(2500);

            log.info("Move stepper (0, 4) to 1000");
            kugelmatik.setStepper(0, 4, 1000);
            kugelmatik.sendMovementData();

            sleep(5000);

            log.info("Move all steppers to 0");
            kugelmatik.setAllSteppers(0);
            kugelmatik.sendMovementData(false, true);

            sleep(5000);

            log.info("Move stepper array (same height) to 500");
            kugelmatik.setStepper(0, 4, 500);
            kugelmatik.setStepper(0, 5, 500);
            kugelmatik.sendMovementData();

            sleep(5000);

            log.info("Move stepper array (different height) to 1000 and 250");
            kugelmatik.setStepper(0, 4, 1000);
            kugelmatik.setStepper(0, 5, 250);
            kugelmatik.sendMovementData();

            log.info("========================================================");
            log.info("Done....");
            while(true);
        }
        catch(Exception e) {
            log.error("Whoooopps!");
            e.printStackTrace();
        }
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
