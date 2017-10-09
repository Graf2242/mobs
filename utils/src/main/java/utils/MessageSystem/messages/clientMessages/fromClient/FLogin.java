package utils.MessageSystem.messages.clientMessages.fromClient;

import base.frontend.Frontend;
import base.masterService.Message;
import base.masterService.nodes.Node;

public class FLogin extends Message {
    final String login;
    final String pass;


    public FLogin(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        frontend.addUser(login, pass, outSocket);
    }
}
