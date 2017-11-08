package utils.MessageSystem.messages.GameMechanics;

import base.gameMechanics.GameMechanics;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.logger.LoggerImpl;

import java.util.Set;

public class GMStartSession extends _GameMechanicsMessageTemplate {
    private final Set<Long> userIds;
    private Logger logger = LoggerImpl.getLogger();

    public GMStartSession(Address from, Set<Long> userIds) {
        super(from);
        logger.info("Created GMStartSession");
        this.userIds = userIds;
    }

    @Override
    public void exec(Node node) {
        GameMechanics gameMechanics = null;
        try {
            gameMechanics = (GameMechanics) node;
        } catch (Exception ex) {
            logger.error(node);

            logger.traceEntry();
            logger.error(ex);
            logger.traceExit();
        }
        if (!gameMechanics.hasSession(userIds)) {
            logger.info("Session started! UserId" + userIds);
            gameMechanics.registerGMSession(userIds);
        }
    }
}
