package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.istack.internal.NotNull;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created by Hendrik on 03.09.2015.
 * Ermöglicht das Erstellen von Logs
 */
public class Log {

    private PrintStream out = null;
    private PrintStream err = null;

    private boolean useSeparateErrStream = false;

    private LogLevel logLevel;

    /**
     * Erstellt eine neie Log-Instanz
     * @param logLevel Der minimale Loglevel für eine Nachricht
     */
    public Log(LogLevel logLevel){
        this.out = System.out;
        this.err = System.err;
        this.logLevel = logLevel;
    }

    /**
     * Erstellt eine neie Log-Instanz
     * @param out Der PrintStream über der statt System.out verwendet werden soll
     * @param logLevel Der minimale Loglevel für eine Nachricht
     */
    public Log(@NotNull PrintStream out, LogLevel logLevel){
        this.out = out;
        this.err = System.err;
        this.logLevel = logLevel;
    }

    /**
     * Erstellt eine neie Log-Instanz
     * @param out Der PrintStream über der statt System.out verwendet werden soll
     * @param err Der PrintStream über der statt System.err verwendet werden soll wenn useSeparateErrStream==true
     * @param logLevel Der minimale Loglevel für eine Nachricht
     */
    public Log(@NotNull PrintStream out, @NotNull PrintStream err, LogLevel logLevel){
        this.out = out;
        this.err = err;
        this.useSeparateErrStream = true;
        this.logLevel = logLevel;
    }

    /**
     * Schreibt eine Nachricht mit dem LogLevel Debug
     * @param message Die zu schreibende Nachricht
     */
    public synchronized void Debug(String message){
        Write(LogLevel.Log, message);
    }

    /**
     * Schreibt die Nachricht einer Exception mit dem LogLevel Err
     * @param ex Die zu schreibende Exception
     */
    public synchronized void Err(Exception ex){
        Err(ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Schreibt eine Nachricht mit dem LogLevel Err
     * @param message Die zu schreibende Nachricht
     */
    public synchronized void Err(String message){
        Write(LogLevel.Err, message);
    }

    /**
     * Schreibt eine Nachricht mit dem LogLevel Verbose
     * @param message Die zu schreibende Nachricht
     */
    public synchronized void Verbose(String message){
        Write(LogLevel.Verbose, message);
    }

    /**
     * Schreibt eine Nachricht mit dem angegebenen LogLevel
     * @param level Der LogLevel für die Nachricht
     * @param message Die zu schreibende Nachricht
     */
    public synchronized void Write(LogLevel level, String message){
        String output = String.format("(%s) [%s] \"%s\"", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), level.name(), message);

        if(level.compareTo(logLevel) >= 0){
            synchronized(System.out){
                if(level == LogLevel.Err && useSeparateErrStream){
                    err.println(output);
                }else{
                    out.println(output);
                }
            }
        }
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setUseSeparateErrStream(boolean useSeparateErrStream) {
        this.useSeparateErrStream = useSeparateErrStream;
    }

    public boolean getUseSeparateErrStream() {
        return useSeparateErrStream;
    }

    public PrintStream getErr() {
        return err;
    }

    public void setErr(@NotNull PrintStream err) {
        this.err = err;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(@NotNull PrintStream out) {
        this.out = out;
    }

    public enum LogLevel{
        Verbose,
        Log,
        Err
    }
}
