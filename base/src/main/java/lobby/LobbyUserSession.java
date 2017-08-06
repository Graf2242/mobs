package lobby;

public interface LobbyUserSession {
    boolean isInFight();

    void setInFight(boolean inFight);

    Long getSessionId();

    void setSessionId(Long sessionId);

    Long getUserId();

    String getUserName();
}
