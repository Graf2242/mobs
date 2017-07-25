package graf.server.GameMechanics;

import graf.server.GameMechanics.Mechanics.Fight;

import java.util.Date;
import java.util.Set;

public class GameMechanicsSession {
    private final Set<Long> userIds;
    private final Long startTime;
    private Fight fight;

    public GameMechanicsSession(Set<Long> userIds) {
        this.userIds = userIds;
        startTime = new Date().getTime();

    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getSessionTime() {
        Long time = new Date().getTime();
        return time - startTime;
    }

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }
}
