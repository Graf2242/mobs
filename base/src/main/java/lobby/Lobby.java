package lobby;

import graf.server.Base.MasterService;
import graf.server.Base.Node;

import java.util.Set;

public interface Lobby extends Node, Runnable {
    Integer getFIGHT_CAPACITY();

    Set<LobbyUserSession> getUsers();

    MasterService getMasterService();

    void registerUser(LobbyUserSession userInfo);

    void registerIfExists(Long userId, String userName);
}
