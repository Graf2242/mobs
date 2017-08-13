package frontend;

import masterService.nodes.Address;
import masterService.nodes.Node;

import java.net.Socket;
import java.util.Map;

public interface Frontend extends Node, Runnable {
    Map<Long, FrontendUserSession> getSessions();

    Socket getMasterService();

    void updateUserId(Long sessionId, Long userId);

    void updateSessionStatus(Long sessionId, UserSessionStatus status);

    FrontendUserSession getSessionByUserId(Long userId);

    Long getSessionId(String userName);

    FrontendUserSession getSessionBySessionId(Long sessionId);

    Address getAddress();

}
