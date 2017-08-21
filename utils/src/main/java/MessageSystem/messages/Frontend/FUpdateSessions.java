package MessageSystem.messages.Frontend;

import frontend.Frontend;
import frontend.FrontendUserSession;
import frontend.UserSessionStatus;
import masterService.nodes.Address;
import masterService.nodes.Node;

import java.util.Map;

public class FUpdateSessions extends _FrontendMessageTemplate {
    private final Map<Long, Long> sessions;
    private final Long userId;
    private final UserSessionStatus status;
    int type;

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
        if (type == 1) {
            if (sessions == null) {
                return;
            }
            Frontend finalFrontend = frontend;
            sessions.forEach((userId, sessionTime) -> {
                System.out.println("Session userId: " + userId + "updated. New Status: " + status);
                FrontendUserSession frontendUserSession = finalFrontend.getSessionByUserId(userId);
                frontendUserSession.setStatus(status);
                frontendUserSession.setSessionTime(sessionTime);

            });
        } else if (type == 2) {
            FrontendUserSession session = frontend.getSessionByUserId(this.userId);
            session.setStatus(status);
        }
    }
}
