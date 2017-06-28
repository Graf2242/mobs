package graf.server.MasterService.messages.Lobby;

import graf.server.Base.Address;
import graf.server.Base.Lobby;
import graf.server.Base.Node;
import graf.server.Frontend.UserSessionStatus;
import graf.server.Lobby.LobbyUserSession;
import graf.server.MasterService.messages.Frontend.FUpdateSessions;

public class LAddUser extends _LobbyMessageTemplate {
    private final Integer userId;
    private final String userName;
    private final Integer sessionId;


    public LAddUser(Address from, Integer userId, String userName, Integer sessionId) {
        super(from);
        this.userId = userId;
        this.userName = userName;
        this.sessionId = sessionId;
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
        LobbyUserSession lobbyUserInfo = new LobbyUserSession(userId, userName);
        lobbyUserInfo.setSessionId(sessionId);
        lobby.registerUser(lobbyUserInfo);
        lobby.getMasterService().addMessage(new FUpdateSessions(lobby.getAddress(), userId, UserSessionStatus.Lobby));
    }
}
