package main;

import frontend.FrontendUserSession;
import frontend.UserSessionStatus;

import java.util.concurrent.atomic.AtomicLong;

public class FrontendUserSessionImpl implements FrontendUserSession {
    private static AtomicLong atomicLongSessions = new AtomicLong();
    private String userName;
    private UserSessionStatus status = UserSessionStatus.CONNECTED;
    private Long sessionId;
    private Long userId = null;
    private Long sessionTime;

    public FrontendUserSessionImpl() {
        this.sessionId = atomicLongSessions.getAndIncrement();
    }

    @Override
    public Long getSessionTime() {
        return sessionTime;
    }

    @Override
    public void setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
    }

    @Override
    public UserSessionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(UserSessionStatus status) {
        this.status = status;
    }

    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
