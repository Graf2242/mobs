package utils.logger;

import org.apache.logging.log4j.Logger;

public class UncaughtExceptionLog4j2Handler implements Thread.UncaughtExceptionHandler {
    private Logger log;

    public UncaughtExceptionLog4j2Handler(Logger log) {
        this.log = log;

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.catching(e);
    }
}
