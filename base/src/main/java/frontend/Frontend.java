package frontend;

import graf.server.Base.Address;
import graf.server.Base.MasterService;
import graf.server.Base.Node;

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
