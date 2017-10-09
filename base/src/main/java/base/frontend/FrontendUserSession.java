package base.frontend;

import java.net.Socket;

public interface FrontendUserSession {
    Socket getUserSocket();

    void setUserSocket(Socket userSocket);

    Long getSessionTime();

    void setSessionTime(Long sessionTime);

    UserSessionStatus getStatus();

    void setStatus(UserSessionStatus status);

    Long getSessionId();

    Long getUserId();

    void setUserId(Long userId);

    String getUserName();

    void setUserName(String userName);
}
