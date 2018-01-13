package utils.MessageSystem.messages.DBService;


import base.databaseService.DBService;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FAddUserMessage;
import utils.MessageSystem.messages.Frontend.FWrongLoginData;

public class DBCreateUser extends _DBServiceMessageTemplate {
    private String login;
    private String pass;
    private Long sessionId;

    public DBCreateUser(Address from, String login, String pass, Long sessionId) {
        super(from);
        this.login = login;
        this.pass = pass;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        DBService dbService = (DBService) node;
        Long userId = dbService.createAccount(login, pass);
        if (userId < 0) {
            NodeMessageSender.sendMessage(dbService.getMasterService(), new FWrongLoginData(dbService.getAddress(), sessionId));
        }
        NodeMessageSender.sendMessage(dbService.getMasterService(), new FAddUserMessage(getFrom(), userId, sessionId));
    }
}
