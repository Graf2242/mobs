package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.logger.LoggerImpl;

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
        Logger logger = LoggerImpl.getLogger();
        Frontend frontend = null;
        try {
            frontend = (Frontend) node;
        } catch (Exception ex) {
            logger.error(node);
            logger.error(ex);
        }
        if (frontend == null) {
            return;
        }
        frontend.updateUserId(sessionId, userId);
    }
}
