package utils.tickSleeper;

import org.apache.logging.log4j.Logger;
import utils.logger.LoggerImpl;

import java.util.Date;

public class TickSleeper {
    private Long TICK_TIME_MS = 10L;
    private Long tickStartTime;
    private Long tickTime;
    private Logger log = LoggerImpl.getLogger();

    public Long getTickTimeMs() {
        return TICK_TIME_MS;
    }

    public void setTickTimeMs(Long tickTimeMs) {
        TICK_TIME_MS = tickTimeMs;
    }

    public void tickStart() {
        tickStartTime = new Date().getTime();
    }

    public void tickEnd() {
        Date date = new Date();
        tickTime = date.getTime() - tickStartTime;
        tickSleep();
    }

    private void tickSleep() {
        long millis = TICK_TIME_MS - tickTime;
        if (millis < 0) {
            log.info("Too long tick:" + tickTime + " in Thread " + Thread.currentThread().getName());
        } else {

            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }
}
