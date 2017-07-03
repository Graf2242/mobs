package graf.server.Base;

import graf.server.Frontend.FrontendUserSession;

public interface Frontend extends Node, Runnable {
    MasterService getMasterService();

    boolean updateUserId(String userName, Integer userId);

    FrontendUserSession getSessionByUserId(Integer userId);

    Integer getSessionId(String userName);

    Address getAddress();

}
