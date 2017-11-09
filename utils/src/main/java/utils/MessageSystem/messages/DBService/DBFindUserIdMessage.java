package utils.MessageSystem.messages.DBService;

import base.databaseService.DBService;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FAddUserMessage;
import utils.logger.LoggerImpl;

import java.util.Objects;

public class DBFindUserIdMessage extends _DBServiceMessageTemplate {
    private final String userName;
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
        Logger logger = LoggerImpl.getLogger();
        DBService dbService = null;
        try {
            dbService = (DBService) node;
        } catch (Exception ex) {
            logger.error(node);

            logger.traceEntry();
            logger.error(ex);
            logger.traceExit();
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
