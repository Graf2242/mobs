package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.Message;
import base.masterService.nodes.Node;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Lobby.LAddUser;

public class FConnectLobby extends Message {
    private final Long sessionId;

    public FConnectLobby(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        NodeMessageSender.sendMessage(frontend.getMasterService(), new LAddUser(getFrom(), frontend.getSessionBySessionId(sessionId).getUserId(), frontend.getSessionBySessionId(sessionId).getUserName()));
    }
}
