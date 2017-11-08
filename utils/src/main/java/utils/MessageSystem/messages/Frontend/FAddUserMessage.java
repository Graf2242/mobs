package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Lobby.LAddUser;
import utils.logger.LoggerImpl;

public class FAddUserMessage extends _FrontendMessageTemplate {
    private final Long userId;
    private final Long sessionId;
    private Logger logger = LoggerImpl.getLogger();

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
            logger.error(node);

            logger.traceEntry();
            logger.error(ex);
            logger.traceExit();
        }
        if (frontend == null) {
            return;
        }
        frontend.updateUserId(sessionId, userId);
        NodeMessageSender.sendMessage(frontend.getMasterService(), new LAddUser(getFrom(), userId, frontend.getSessionByUserId(userId).getUserName()));
    }
}
