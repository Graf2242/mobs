package base.frontend;

import java.io.Serializable;
import java.net.Socket;

public interface FrontendUserSession extends Serializable {
    boolean isNeedUpdate();

    void setNeedUpdate(boolean needUpdate);

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
