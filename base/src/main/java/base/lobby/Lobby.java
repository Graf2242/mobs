package base.lobby;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.net.Socket;
import java.util.Set;

public interface Lobby extends Node, Runnable {
    Integer getFIGHT_CAPACITY();

    Set<LobbyUserSession> getUsers();

    Socket getMasterService();

    Address getAddress();

    void registerUser(LobbyUserSession userInfo);

    void registerIfExists(Long userId, String userName);
}
