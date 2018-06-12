package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.nodes.Node;
import base.utils.Message;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.DBService.DBFindUserIdMessage;

public class FLogin extends Message {
    private final String login;
    private final String pass;


    public FLogin(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        Long sessionId = frontend.addUser(login, pass, outSocket);
        NodeMessageSender.sendMessage(frontend.getMasterService(), new DBFindUserIdMessage(frontend.getAddress(), login, pass, sessionId));

    }
}
