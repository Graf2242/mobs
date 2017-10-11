package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Node;

public class CLoginSuccess extends Message {
    final private Long userId;
    final private UserSessionStatus status;

    public CLoginSuccess(Long userId, UserSessionStatus status) {
        this.userId = userId;
        this.status = status;
    }

    //TODO
    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.setStatus(status);
        client.setUserId(userId);
    }
}
