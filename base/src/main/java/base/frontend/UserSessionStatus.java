package base.frontend;

import java.io.Serializable;

public enum UserSessionStatus implements Serializable {
    CONNECTED,
    IN_LOGIN,
    LOBBY,
    SEARCH,
    FIGHT,
    INACTIVE,
    WRONG_LOGIN_INFO
}
