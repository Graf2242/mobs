package gameMechanics;

import java.util.Set;

public interface GameMechanics extends graf.server.Base.Node, Runnable {
    Set<GameMechanicsSession> getSessions();

    graf.server.Base.MasterService getMasterService();

    void registerGMSession(Set<Long> userIDs);

    boolean hasSession(Set<Long> userIds);
}
