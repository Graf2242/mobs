package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.nodes.Node;
import base.utils.Message;
import utils.logger.LoggerImpl;

public class FDisconnect extends Message {
    final private Long userId;

    public FDisconnect(Long userId) {
        this.userId = userId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        frontend.disconnectUser(userId);
        LoggerImpl.getLogger().info("User disconnected!");
    }
}
