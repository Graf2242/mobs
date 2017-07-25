package graf.server.Base;

public interface DBService extends Node, Runnable {
    MasterService getMasterService();

    Long getAccountId(String userName, String pass, Long sessionId);

    Long createAccount(String userName, String pass);

    Address getAddress();
}
