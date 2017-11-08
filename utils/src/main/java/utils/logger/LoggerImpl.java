package utils.logger;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LoggerImpl {
    private static Logger log;
    private static boolean logCreated = false;

    public static void createLogger(String logName) {
        if (!logCreated) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
            ThreadContext.put("STARTTIME", format.format(new Date()));
            ThreadContext.put("LOGNAME", logName);
            log = LogManager.getLogger("Frontend");
        } else {
            log.warn("Code try to create logger twice");
            printStackTrace();
        }
        logCreated = true;
    }

    public static Logger getLogger() {
        if (!logCreated) {
            System.err.println("Logger not configured");
        }
        return log;
    }

    public static void printStackTrace() {
        log.traceEntry();
        log.catching(Level.WARN, new Exception());
        log.traceExit();
    }
}
