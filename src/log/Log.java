package log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cotix on 4/2/15.
 */
public class Log {
    private static PrintStream logOut = System.out;
    private static Set<LogLevel> enabledLogLevels = new HashSet<LogLevel>();

    public void enableLevel(LogLevel lvl) {
        if (!enabledLogLevels.contains(lvl)) {
            enabledLogLevels.add(lvl);
        }
    }

    public void disableLevel(LogLevel lvl) {
        enabledLogLevels.remove(lvl);
    }

    public void enableAllLevels() {
        for(LogLevel lvl : LogLevel.values()) {
            enableLevel(lvl);
        }
    }

    public void disableAllLevels() {
        enabledLogLevels = new HashSet<LogLevel>();
    }

    public static void Log(String msg, LogLevel lvl) {
        if (enabledLogLevels.contains(lvl)) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
            logOut.println(format.format(now) + " LOG " + lvl.name() + ": " + msg);
        }
    }
}
