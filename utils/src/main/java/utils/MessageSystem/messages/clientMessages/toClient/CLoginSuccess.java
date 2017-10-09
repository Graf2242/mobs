package utils.MessageSystem.messages.clientMessages.toClient;

import base.masterService.Message;
import base.masterService.nodes.Node;

public class CLoginSuccess extends Message {
    final private Long userId;

    public CLoginSuccess(Long userId) {
        this.userId = userId;
    }

    //TODO
    @Override
    public void exec(Node node) {

    }
}
