package base.Client;

import base.frontend.UserSessionStatus;
import base.masterService.nodes.Node;

import java.net.Socket;

public interface Client extends Node {
    void setUserId(Long userId);

    void setStatus(UserSessionStatus status);

    Socket getFrontendSocket();

    void waitForStatusUpdate();
}
