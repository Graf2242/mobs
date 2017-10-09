package main;

import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class FrontendUserSessionImpl implements FrontendUserSession {
    private static AtomicLong atomicLongSessions = new AtomicLong();
    private String userName;
    private UserSessionStatus status = UserSessionStatus.CONNECTED;
    private Long sessionId;
    private Long userId = null;
    private Long sessionTime;

    private Socket userSocket;

    @Override
    public Socket getUserSocket() {
        return userSocket;
    }

    @Override
    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }

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
        System.out.println("Set status = " + status);
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
