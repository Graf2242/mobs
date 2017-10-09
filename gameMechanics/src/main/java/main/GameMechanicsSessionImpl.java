package main;

import base.gameMechanics.Fight;
import base.gameMechanics.GameMechanicsSession;

import java.util.Date;
import java.util.Set;

public class GameMechanicsSessionImpl implements GameMechanicsSession {
    private final Set<Long> userIds;
    private final Long startTime;
    private transient Fight fight;

    public GameMechanicsSessionImpl(Set<Long> userIds) {
        this.userIds = userIds;
        startTime = new Date().getTime();

    }

    @Override
    public Long getStartTime() {
        return startTime;
    }

    @Override
    public Long getSessionTime() {
        Long time = new Date().getTime();
        return time - startTime;
    }

    @Override
    public Fight getFight() {
        return fight;
    }

    @Override
    public void setFight(Fight fight) {
        this.fight = fight;

    }

    @Override
    public Set<Long> getUserIds() {
        return userIds;
    }
}
