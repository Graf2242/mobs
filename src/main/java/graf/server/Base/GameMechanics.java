package graf.server.Base;

import java.util.Set;

public interface GameMechanics extends Node, Runnable {
    void registerGMSession(Set<Integer> userIDs);

}
