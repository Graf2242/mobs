package graf.server.MasterService.messages.Frontend;

import graf.server.Base.Address;
import graf.server.Base.Frontend;
import graf.server.Base.Node;
import graf.server.Frontend.FrontendUserSession;
import graf.server.Frontend.UserSessionStatus;
import graf.server.GameMechanics.GameMechanicsSession;

import java.util.Set;

public class FUpdateSessions extends _FrontendMessageTemplate {
    private final Set<GameMechanicsSession> sessions;
    private final Long userId;
    private final UserSessionStatus status;

    //TODO Перевести на sessionID
    public FUpdateSessions(Address from, Long userId, UserSessionStatus status) {
        super(from);
        this.userId = userId;
        this.status = status;
        sessions = null;
    }

    public FUpdateSessions(Address from, Set<GameMechanicsSession> sessions, UserSessionStatus status) {
        super(from);
        this.sessions = sessions;
        userId = null;
        this.status = status;
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
        if (sessions != null) {
            for (GameMechanicsSession session : sessions) {
                for (Long userId : session.getUserIds()) {
                    FrontendUserSession frontendUserSession = frontend.getSessionByUserId(userId);
                    frontendUserSession.setStatus(status);
                    Long sessionTime = session.getSessionTime();
                    frontendUserSession.setSessionTime(sessionTime);
                    //AnyMoreDataHere
                }
            }
        } else {
            FrontendUserSession session = frontend.getSessionByUserId(this.userId);
            session.setStatus(status);
        }
    }
}
