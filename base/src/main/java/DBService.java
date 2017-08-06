package graf.server.Base;

public interface DBService extends graf.server.Base.Node, Runnable {
    graf.server.Base.MasterService getMasterService();

    Long getAccountId(String userName, String pass, Long sessionId);

    @SuppressWarnings("UnusedReturnValue")
    Long createAccount(String userName, String pass);
}