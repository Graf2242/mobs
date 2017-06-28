package graf.server.Base;

public interface AccountService extends Node, Runnable {
    MasterService getMasterService();

    Integer getUserId(String userName);

    Address getAddress();
}
