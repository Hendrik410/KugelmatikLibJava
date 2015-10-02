package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hendrik on 01.09.2015.
 * Hilfsklasse um Aufgaben wie Ping regelmäßig aufzurufen
 */
public class KugelmatikScheduler {

    private Kugelmatik kugelmatik;
    private Timer timer;

    /**
     * Erstellt eine neue Scheduler-Instanz
     * @param kugelmatik Die Kugelmatik für welche die Aktionen ausgeführt werden sollen
     */
    public KugelmatikScheduler(@NotNull Kugelmatik kugelmatik){
        this.kugelmatik = kugelmatik;
        timer = new Timer();
    }

    /**
     * Ruft SendPing() in einem gewissen Interval auf
     * @param interval Das Interval in dem SendPing aufgerufen werden soll
     */
    public void SchedulePing(long interval){
        ScheduleTimerTask(new PingTask(), interval);
    }

    /**
     * Ruft ResenPackets() in einem gewissen Interval auf
     * @param interval Das Interval in dem ResendPackets aufgerufen werden soll
     */
    public void ScheduleResend(long interval){
        ScheduleTimerTask(new ResendTask(), interval);
    }

    /**
     * Ruft einen TimerTask in einem gewissen Interval auf
     * @param task Der aufzurufende TimerTask
     * @param interval Das Interval in dem der TimerTask aufgerufen werden soll
     */
    protected void ScheduleTimerTask(@NotNull TimerTask task, long interval){
        if(interval <= 0)
            throw new InvalidParameterException("interval must be greater than 0");

        timer.scheduleAtFixedRate(task, 0, interval);
    }

    /**
     * Löscht alle vorher definierten Tasks
     */
    public void RemoveAll(){
        timer.purge();
    }

    /**
     * Gibt das Kugelmatik object zurück, welches mit dem Schedule verküpft ist
     */
    public Kugelmatik getKugelmatik() {
        return kugelmatik;
    }

    private class PingTask extends TimerTask{
        @Override
        public void run() {
            kugelmatik.SendPing();
        }
    }

    private class ResendTask extends TimerTask{
        @Override
        public void run() {
            kugelmatik.ResendPackets();
        }
    }
}
