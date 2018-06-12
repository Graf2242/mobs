package base.gameMechanics;

import base.masterService.nodes.Node;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

public interface GameMechanics extends Node, Runnable {
    Set<GameMechanicsSession> getSessions();

    Socket getMasterService();

    void connectClient(String ip, int port, Long userId);

    void registerGMSession(Set<Long> userIDs) throws UnknownHostException;

    boolean hasSession(Set<Long> userIds);
}
