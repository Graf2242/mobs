package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Node;

//Добавить создание этого сообщения во фронтенде каждый тик.
public class CUpdateSession extends Message {
    final private String userName;
    final private Long userID;
    final private UserSessionStatus status;
    final private Long fightTime;

    public CUpdateSession(FrontendUserSession userSession) {
        this.userID = userSession.getUserId();
        this.userName = userSession.getUserName();
        this.status = userSession.getStatus();
        this.fightTime = userSession.getSessionTime();
    }

    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.updateUserSession(userID, userName, status, fightTime);
    }
}
