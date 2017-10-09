package base.gameMechanics;

import base.masterService.nodes.Node;

import java.net.Socket;
import java.util.Set;

public interface GameMechanics extends Node, Runnable {
    Set<GameMechanicsSession> getSessions();

    Socket getMasterService();

    void registerGMSession(Set<Long> userIDs);

    boolean hasSession(Set<Long> userIds);
}
