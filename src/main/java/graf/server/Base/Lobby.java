package graf.server.Base;

import graf.server.Lobby.LobbyUserSession;

import java.util.Set;

public interface Lobby extends Node, Runnable {
    Integer getFIGHT_CAPACITY();

    Set<LobbyUserSession> getUsers();

    MasterService getMasterService();

    void registerUser(LobbyUserSession userInfo);
}
