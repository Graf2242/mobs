package utils.logger;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class LoggerImpl {
    private static Logger log = null;

    public static Logger createLogger(String logName) {
        if (Objects.equals(log, null)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
            ThreadContext.put("STARTTIME", format.format(new Date()));
            ThreadContext.put("LOGNAME", logName);
            log = LogManager.getLogger("file");
        } else {
            log.warn("Code try to create logger twice");
            printStackTrace(log);
        }
        return log;
    }

    public static Logger getLogger() {
        if (Objects.equals(log, null)) {
            createLogger("BaseLog");
            log.fatal(new Exception("Call not configured logger!"));
        }
        return log;
    }

    private static void printStackTrace(Logger log) {
        log.catching(Level.WARN, new Exception());
    }

}

