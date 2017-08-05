package graf.server.Lobby;

import java.util.concurrent.atomic.AtomicLong;

public class LobbyUserSession {
    private static AtomicLong atomicLong = new AtomicLong();
    private final Long userId;
    private final String userName;

    private boolean isInFight = false;

    public boolean isInFight() {
        return isInFight;
    }

    public void setInFight(boolean inFight) {
        isInFight = inFight;
    }
    private Long sessionId;

    public LobbyUserSession(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.sessionId = atomicLong.getAndIncrement();
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
