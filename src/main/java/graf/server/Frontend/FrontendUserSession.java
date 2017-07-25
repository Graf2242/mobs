package graf.server.Frontend;

import java.util.concurrent.atomic.AtomicLong;

public class FrontendUserSession {
    private static AtomicLong atomicLong = new AtomicLong();
    private String userName;
    private UserSessionStatus status = UserSessionStatus.CONNECTED;
    private Long sessionId;
    private Long userId = null;
    private Long sessionTime;

    public FrontendUserSession() {
        this.userName = userName;
        this.sessionId = atomicLong.getAndIncrement();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public UserSessionStatus getStatus() {
        return status;
    }

    public void setStatus(UserSessionStatus status) {
        this.status = status;
    }

    public Long getSessionId() {
        return sessionId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
}
