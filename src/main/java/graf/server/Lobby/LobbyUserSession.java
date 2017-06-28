package graf.server.Lobby;

public class LobbyUserSession {
    private final Integer userId;
    private final String userName;
    boolean isInFight = false;
    private Integer sessionId;

    public LobbyUserSession(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
