package lobby;

import masterService.Address;
import masterService.MasterService;
import masterService.Node;

import java.util.Set;

public interface Lobby extends Node, Runnable {
    Integer getFIGHT_CAPACITY();

    Set<LobbyUserSession> getUsers();

    MasterService getMasterService();

    Address getAddress();

    void registerUser(LobbyUserSession userInfo);

    void registerIfExists(Long userId, String userName);
}
