package base.gameMechanics;

import java.io.Serializable;
import java.util.Set;

public interface GameMechanicsSession extends Serializable {
    Long getStartTime();

    Long getSessionTime();

    Fight getFight();

    void setFight(Fight fight);

    Set<Long> getUserIds();
}
