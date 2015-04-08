package log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Log {
    private static PrintStream logOut = System.out;
    private static Set<LogLevel> enabledLogLevels = new HashSet<LogLevel>();

    public static void enableLevel(LogLevel lvl) {
        if (!enabledLogLevels.contains(lvl)) {
            enabledLogLevels.add(lvl);
        }
    }

    public static void disableLevel(LogLevel lvl) {
        enabledLogLevels.remove(lvl);
    }

    public static void enableAllLevels() {
        for(LogLevel lvl : LogLevel.values()) {
            enableLevel(lvl);
        }
    }

    public static void disableAllLevels() {
        enabledLogLevels = new HashSet<LogLevel>();
    }

    public static void log(String msg, LogLevel lvl) {
        if (enabledLogLevels.contains(lvl)) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
            logOut.println(format.format(now) + " LOG " + lvl.name() + ": " + msg);
        }
    }
}
