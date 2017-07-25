package graf.server.Base;

import java.util.Set;

public interface GameMechanics extends Node, Runnable {
    void registerGMSession(Set<Long> userIDs);

    boolean hasSession(Set<Long> userIds);
}
