package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.nodes.Node;
import base.utils.Message;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.DBService.DBCreateUser;

public class FCreateAccount extends Message {

    private String login;
    private String pass;

    public FCreateAccount(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        Long sessionId = frontend.addUser(login, pass, outSocket);
        NodeMessageSender.sendMessage(frontend.getMasterService(), new DBCreateUser(frontend.getAddress(), login, pass, sessionId));
    }
}
