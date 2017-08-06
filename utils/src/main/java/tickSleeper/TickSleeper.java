package tickSleeper;

import java.util.Date;

public class TickSleeper {
    private static Long TICK_TIME_MS = 10L;
    Date date = new Date();
    private Long tickStartTime;
    private Long tickTime;

    public static Long getTickTimeMs() {
        return TICK_TIME_MS;
    }

    public void setTickTimeMs(Long tickTimeMs) {
        TICK_TIME_MS = tickTimeMs;
    }

    public void tickStart() {
        tickStartTime = date.getTime();
    }

    public void tickEnd() {
        tickTime = date.getTime() - tickStartTime;
        tickSleep();
    }

    private void tickSleep() {
        try {
            Thread.sleep(TICK_TIME_MS - tickTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
