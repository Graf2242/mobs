package base.Client;

import base.frontend.UserSessionStatus;

import java.net.Socket;

public interface Client {
    void setStatus(UserSessionStatus status);

    Socket getFrontendSocket();

    void waitForStatusUpdate();
}
