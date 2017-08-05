package graf.server.Base;

import graf.server.GameMechanics.GameMechanicsSession;

import java.util.Set;

public interface GameMechanics extends Node, Runnable {
    Set<GameMechanicsSession> getSessions();

    MasterService getMasterService();

    void registerGMSession(Set<Long> userIDs);

    boolean hasSession(Set<Long> userIds);
}
