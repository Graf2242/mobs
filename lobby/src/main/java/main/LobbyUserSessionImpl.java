package main;


import base.lobby.LobbyUserSession;

import java.util.concurrent.atomic.AtomicLong;

public class LobbyUserSessionImpl implements LobbyUserSession {
    private static AtomicLong atomicLong = new AtomicLong();
    private final Long userId;
    private final String userName;
    private boolean isInFight = false;

    public LobbyUserSessionImpl(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.sessionId = atomicLong.getAndIncrement();
    }

    @Override
    public boolean isInFight() {
        return isInFight;
    }

    private Long sessionId;

    @Override
    public void setInFight(boolean inFight) {
        isInFight = inFight;
    }

    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
