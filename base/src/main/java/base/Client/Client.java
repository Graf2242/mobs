package base.Client;

import base.frontend.UserSessionStatus;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public interface Client extends Node {
    Logger getLog();

    Socket getFrontendSocket();

    void waitForStatusUpdate();

    void updateUserSession(Long userID, String userName, UserSessionStatus status, Long fightTime);

    void setStatus(UserSessionStatus status);

    void setUserId(Long userId);

    void setSessionId(Long userSessionId);
}
