package graf.server.Base;

import graf.server.Frontend.FrontendUserSession;
import graf.server.Frontend.UserSessionStatus;

import java.util.Map;

public interface Frontend extends Node, Runnable {
    Map<Long, FrontendUserSession> getSessions();

    MasterService getMasterService();

    boolean updateUserId(Long sessionId, Long userId);

    void updateSessionStatus(Long sessionId, UserSessionStatus status);

    FrontendUserSession getSessionByUserId(Long userId);

    Long getSessionId(String userName);

    FrontendUserSession getSessionBySessionId(Long sessionId);

    Address getAddress();

}
