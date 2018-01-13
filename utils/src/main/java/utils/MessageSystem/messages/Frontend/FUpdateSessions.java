package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.toClient.CUpdateSession;
import utils.logger.LoggerImpl;

import java.util.Map;

public class FUpdateSessions extends _FrontendMessageTemplate {
    private final Map<Long, Long> sessions;
    private final Long userId;
    private final UserSessionStatus status;
    private int type;

    public FUpdateSessions(Address from, Long userId, UserSessionStatus status) {
        super(from);
        this.userId = userId;
        this.status = status;
        sessions = null;
        this.type = 2;
    }

    public FUpdateSessions(Address from, Map<Long, Long> sessions) {
        super(from);
        this.sessions = sessions;
        userId = null;
        status = UserSessionStatus.FIGHT;
        this.type = 1;
    }

    @Override
    public void exec(Node node) {
        Logger logger = LoggerImpl.getLogger();
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
        if (type == 1) {
            if (sessions == null) {
                return;
            }
            Frontend finalFrontend = frontend;
            sessions.forEach((userId, sessionTime) -> {
                FrontendUserSession frontendUserSession = finalFrontend.getSessionByUserId(userId);
                frontendUserSession.setStatus(status);
                frontendUserSession.setSessionTime(sessionTime);
                frontendUserSession.setNeedUpdate(true);
            });
        } else if (type == 2) {
            FrontendUserSession session = frontend.getSessionByUserId(this.userId);
            session.setStatus(status);
            NodeMessageSender.sendMessage(session.getUserSocket(), new CUpdateSession(session));
        }
    }
}
