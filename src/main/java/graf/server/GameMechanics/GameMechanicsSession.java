package graf.server.GameMechanics;

import graf.server.GameMechanics.Mechanics.Fight;

import java.util.Set;

public class GameMechanicsSession {
    private final Set<Integer> userIds;
    private Fight fight;

    public GameMechanicsSession(Set<Integer> userIds) {
        this.userIds = userIds;
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
