package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.nodes.Node;
import base.utils.Message;

public class CLoginProcess extends Message {

    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.setStatus(UserSessionStatus.IN_LOGIN);
    }
}
