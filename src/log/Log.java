package log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class enabling multiple levels of logging.
 * This enables a very efficient of keeping track what is happening in the program.
 */
public class Log {
    private static PrintStream logOut = System.out;
    private static Set<LogLevel> enabledLogLevels = new HashSet<LogLevel>();


    /**
     * Enables a specific loglevel.
     * @param lvl The loglevel to be enabled.
     */
    public static void enableLevel(LogLevel lvl) {
        if (!enabledLogLevels.contains(lvl)) {
            enabledLogLevels.add(lvl);
        }
    }

    /**
     * Disables a specific loglevel.
     * @param lvl The loglevel to be disabled.
     */
    public static void disableLevel(LogLevel lvl) {
        enabledLogLevels.remove(lvl);
    }


    /**
     * Enables all loglevels.
      */
    public static void enableAllLevels() {
        for(LogLevel lvl : LogLevel.values()) {
            enableLevel(lvl);
        }
    }

    /**
     * Disables all the loglevels.
     */
    public static void disableAllLevels() {
        enabledLogLevels = new HashSet<LogLevel>();
    }


    /**
     * Logs a String at the desired loglevel.
     * @param msg The message to be logged.
     * @param lvl The level at which you want to log the message.
     */
    public static void log(String msg, LogLevel lvl) {
        if (enabledLogLevels.contains(lvl)) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
            logOut.println(format.format(now) + " LOG " + lvl.name() + ": " + msg);
        }
    }
}
