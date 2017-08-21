package databaseService;

import masterService.nodes.Node;

import java.net.Socket;

public interface DBService extends Node, Runnable {

    Socket getMasterService();

    Long getAccountId(String userName, String pass, Long sessionId);

    @SuppressWarnings("UnusedReturnValue")
    Long createAccount(String userName, String pass);
}