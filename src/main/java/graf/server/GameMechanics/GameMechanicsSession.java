package graf.server.GameMechanics;

import graf.server.GameMechanics.Mechanics.Fight;

import java.util.Date;
import java.util.Set;

public class GameMechanicsSession {
    private final Set<Integer> userIds;
    private final long startTime;
    private Fight fight;

    public GameMechanicsSession(Set<Integer> userIds) {
        this.userIds = userIds;
        startTime = new Date().getTime();

    }

    public long getStartTime() {
        return startTime;
    }

    public long getSessionTime() {
        long time = new Date().getTime();
        return time - startTime;
    }

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }
}
