package base.Client;

import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.nodes.Node;

import java.net.Socket;

public interface Client extends Node {
    FrontendUserSession getFrontendUserSession();

    void setUserId(Long userId);

    void setStatus(UserSessionStatus status);

    Socket getFrontendSocket();

    void waitForStatusUpdate();

    void updateUserSession(Long userID, String userName, UserSessionStatus status, Long fightTime);
}
