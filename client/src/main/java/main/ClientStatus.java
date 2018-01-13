package main;

import base.frontend.UserSessionStatus;

@SuppressWarnings("ALL")
public class ClientStatus {
    private Long userSessionId;
    private UserSessionStatus status;
    private Long userId;

    public Long getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(Long userSessionId) {
        this.userSessionId = userSessionId;
    }

    public UserSessionStatus getStatus() {
        return status;
    }

    public void setStatus(UserSessionStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
