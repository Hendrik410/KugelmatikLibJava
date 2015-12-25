package org.KarlKuebelSchule.KugelmatikLib;

import com.sun.glass.ui.SystemClipboard;
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
    private LogLevel logLevel;

    /**
     * Erstellt eine Log-Instanz mit Standard Stream (System.out).
     *
     * @param logLevel Gibt den mindest LogLevel an, ab dem Log-Einträge angezeigt werden sollen.
     */
    public Log(LogLevel logLevel) {
        this(System.out, logLevel);
    }

    /**
     * Erstellt eine Log-Instanz mit einem anderen Out PrintStream.
     *
     * @param out      Gibt den PrintStream an, über dem Log-Einträge geschrieben werden sollen.
     * @param logLevel Gibt den mindest LogLevel an, ab dem Log-Einträge angezeigt werden sollen.
     */
    public Log(@NotNull PrintStream out, LogLevel logLevel) {
        this(out, null, logLevel);
    }

    /**
     * Erstellt eine Log-Instanz mit einem anderen Out und Err PrintStream.
     *
     * @param out      Gibt den PrintStream an, über dem Log-Einträge geschrieben werden sollen.
     * @param err      Gibt den PrintStream an, über dem Fehler Log-Einträge geschrieben werden sollen.
     * @param logLevel Gibt den mindest LogLevel an, ab dem Log-Einträge angezeigt werden sollen.
     */
    public Log(@NotNull PrintStream out, PrintStream err, LogLevel logLevel) {
        this.out = out;
        this.err = err;
        this.logLevel = logLevel;
    }

    public synchronized void verbose(String message) {
        write(LogLevel.Verbose, message);
    }

    public synchronized void verbose(String format, Object... args) {
        write(LogLevel.Verbose, String.format(format, args));
    }

    public synchronized void debug(String message) {
        write(LogLevel.Debug, message);
    }

    public synchronized void debug(String format, Object... args) {
        write(LogLevel.Debug, String.format(format, args));
    }

    public synchronized void info(String message) {
        write(LogLevel.Info, message);
    }

    public synchronized void info(String format, Object... args) {
        write(LogLevel.Info, String.format(format, args));
    }

    public synchronized void error(String message) {
        write(LogLevel.Error, message);
    }

    public synchronized void error(String format, Object... args) {
        write(LogLevel.Error, String.format(format, args));
    }

    public synchronized void write(LogLevel level, String message) {
        String output = String.format("(%s) [%s] \"%s\"", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), level.name(), message);

        if (level.compareTo(logLevel) >= 0) {
            if (level == LogLevel.Error && err != null) {
                err.println(output);
            } else {
                out.println(output);
            }
        }
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }


    public PrintStream getErr() {
        return err;
    }

    public void setErr(PrintStream err) {
        this.err = err;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(@NotNull PrintStream out) {
        this.out = out;
    }
}
