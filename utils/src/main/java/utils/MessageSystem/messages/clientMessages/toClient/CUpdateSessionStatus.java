package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.nodes.Node;
import base.utils.Message;

public class CUpdateSessionStatus extends Message {
    final private UserSessionStatus status;

    public CUpdateSessionStatus(UserSessionStatus status) {
        this.status = status;
    }

    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.setStatus(status);
    }
}
