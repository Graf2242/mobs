package gameMechanics;

import java.util.Set;

public interface GameMechanicsSession {
    Long getStartTime();

    Long getSessionTime();

    Fight getFight();

    void setFight(Fight fight);

    Set<Long> getUserIds();
}
