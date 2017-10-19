package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Lobby.LAddUser;

public class FAddUserMessage extends _FrontendMessageTemplate {
    private final Long userId;
    private final Long sessionId;

    public FAddUserMessage(Address from, Long userId, Long sessionId) {
        super(from);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = null;
        try {
            frontend = (Frontend) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (frontend == null) {
            return;
        }
        frontend.updateUserId(sessionId, userId);
        NodeMessageSender.sendMessage(frontend.getMasterService(), new LAddUser(getFrom(), userId, frontend.getSessionByUserId(userId).getUserName()));
    }
}
