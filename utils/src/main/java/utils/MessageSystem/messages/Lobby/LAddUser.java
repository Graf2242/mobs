package utils.MessageSystem.messages.Lobby;

import base.lobby.Lobby;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

public class LAddUser extends _LobbyMessageTemplate {
    private final Long userId;
    private final String userName;


    public LAddUser(Address from, Long userId, String userName) {
        super(from);
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void exec(Node node) {
        Lobby lobby = null;
        try {
            lobby = (Lobby) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (lobby == null) {
            return;
        }
        lobby.registerIfExists(userId, userName);
    }
}
