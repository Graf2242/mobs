package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Node;

public class CLoginSuccess extends Message {
    final private Long userId;
    final private UserSessionStatus status;
    final private Long userSessionId;

    public CLoginSuccess(Long userId, Long userSessionId, UserSessionStatus status) {
        this.userId = userId;
        this.status = status;
        this.userSessionId = userSessionId;
    }

    //TODO
    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.setStatus(status);
        client.setUserId(userId);
        client.setSessionId(userSessionId);
    }
}
