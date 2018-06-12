package utils.MessageSystem.messages.GameMechanics;

import base.gameMechanics.GameMechanics;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Metrics.MetricsIncrement;
import utils.logger.LoggerImpl;

import java.net.UnknownHostException;
import java.util.Set;

public class GMStartSession extends _GameMechanicsMessageTemplate {
    private final Set<Long> userIds;

    public GMStartSession(Address from, Set<Long> userIds) {
        super(from);
        LoggerImpl.getLogger().info("Created GMStartSession");
        this.userIds = userIds;
    }

    @Override
    public void exec(Node node) {
        Logger logger = LoggerImpl.getLogger();
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
            try {
                gameMechanics.registerGMSession(userIds);
                NodeMessageSender.sendMessage(node.getMasterService(), new MetricsIncrement(node.getAddress(), "CCUMechenics"));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
