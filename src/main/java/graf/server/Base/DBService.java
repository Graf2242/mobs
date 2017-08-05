package graf.server.Base;

public interface DBService extends Node, Runnable {
    MasterService getMasterService();

    Long getAccountId(String userName, String pass, Long sessionId);

    @SuppressWarnings("UnusedReturnValue")
    Long createAccount(String userName, String pass);
}