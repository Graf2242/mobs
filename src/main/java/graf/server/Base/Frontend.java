package graf.server.Base;

import graf.server.Frontend.FrontendUserSession;
import graf.server.Frontend.UserSessionStatus;

public interface Frontend extends Node, Runnable {
    MasterService getMasterService();

    boolean updateUserId(Long sessionId, Long userId);

    void updateSessionStatus(Long sessionId, UserSessionStatus status);

    FrontendUserSession getSessionByUserId(Long userId);

    Long getSessionId(String userName);

    FrontendUserSession getSessionBySessionId(Integer userId);

    Address getAddress();

}
