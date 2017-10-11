package utils.MessageSystem.messages.DBService;

import base.databaseService.DBService;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FAddUserMessage;

import java.util.Objects;

public class DBFindUserIdMessage extends _DBServiceMessageTemplate {
    final String userName;
    private final String pass;
    private final Long sessionId;

    public DBFindUserIdMessage(Address from, String userName, String pass, Long sessionId) {
        super(from);
        this.userName = userName;
        this.pass = pass;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Node node) {
        DBService dbService = null;
        try {
            dbService = (DBService) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (dbService == null) {
            return;
        }
        Long userId = dbService.getAccountId(userName, pass, sessionId);
        if (!Objects.equals(userId, null)) {
            NodeMessageSender.sendMessage(dbService.getMasterService(), new FAddUserMessage(dbService.getAddress(), userId, sessionId));
        }
    }
}
