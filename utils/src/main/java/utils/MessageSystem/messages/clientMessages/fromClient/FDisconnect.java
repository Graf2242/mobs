package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.Message;
import base.masterService.nodes.Node;

public class FDisconnect extends Message {
    final private Long userId;

    public FDisconnect(Long userId) {
        this.userId = userId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        frontend.disconnectUser(userId);
        System.out.println("User disconnected!");
    }
}
