package base.frontend;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.net.Socket;
import java.util.Map;

public interface Frontend extends Node, Runnable {
    Map<Long, FrontendUserSession> getSessions();

    Socket getMasterService();

    Long getSessionId(Long userId);

    Long addUser(String login, String pass, Socket clientSocket);

    void updateUserId(Long sessionId, Long userId);

    void updateSessionStatus(Long sessionId, UserSessionStatus status);

    FrontendUserSession getSessionByUserId(Long userId);

    Long getSessionId(String userName);

    FrontendUserSession getSessionBySessionId(Long sessionId);

    Address getAddress();

    void disconnectUser(Long userId);
}
