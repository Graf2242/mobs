package utils.MessageSystem.messages.GameMechanics;

import base.frontend.FrontendUserSession;
import base.gameMechanics.GameMechanics;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.util.Map;

public class GMSendUserSockets extends _GameMechanicsMessageTemplate {

    private Map<Long, FrontendUserSession> sessions;

    public GMSendUserSockets(Address from, Map<Long, FrontendUserSession> sessions) {
        super(from);
        this.sessions = sessions;
    }

    @Override
    public void exec(Node node) {
        GameMechanics gameMechanics = (GameMechanics) node;
        sessions.forEach((aLong, frontendUserSession) -> gameMechanics.connectClient(frontendUserSession.getIp(), frontendUserSession.getPort(), frontendUserSession.getUserId()));
    }
}
