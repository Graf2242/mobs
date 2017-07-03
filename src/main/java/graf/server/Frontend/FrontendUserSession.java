package graf.server.Frontend;

public class FrontendUserSession {
    private final String userName;
    UserSessionStatus status = UserSessionStatus.Connected;
    private Integer sessionId;
    private Integer userId;
    private long sessionTime;

    public FrontendUserSession(String userName) {
        this.userName = userName;
    }

    public long getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public UserSessionStatus getStatus() {
        return status;
    }

    public void setStatus(UserSessionStatus status) {
        this.status = status;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
}
