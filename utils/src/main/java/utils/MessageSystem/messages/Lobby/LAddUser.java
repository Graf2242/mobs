package utils.MessageSystem.messages.Lobby;

import base.lobby.Lobby;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.logger.LoggerImpl;

public class LAddUser extends _LobbyMessageTemplate {
    private final Long userId;
    private final String userName;
    private Logger logger = LoggerImpl.getLogger();


    public LAddUser(Address from, Long userId, String userName) {
        super(from);
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void exec(Node node) {
        Lobby lobby = null;
        try {
            lobby = (Lobby) node;
        } catch (Exception ex) {
            logger.error(node);

            logger.traceEntry();
            logger.error(ex);
            logger.traceExit();
        }
        if (lobby == null) {
            return;
        }
        lobby.registerIfExists(userId, userName);
    }
}
