package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.nodes.Node;
import base.utils.Message;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Lobby.LAddUser;
import utils.MessageSystem.messages.Metrics.MetricDecrement;

public class FConnectLobby extends Message {
    private final Long sessionId;

    public FConnectLobby(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        NodeMessageSender.sendMessage(node.getMasterService(), new MetricDecrement(node.getAddress(), "CCUFrontend", "Count of Connected Users on Frontend"));
        NodeMessageSender.sendMessage(frontend.getMasterService(), new LAddUser(getFrom(), frontend.getSessionBySessionId(sessionId).getUserId(), frontend.getSessionBySessionId(sessionId).getUserName()));
    }
}
