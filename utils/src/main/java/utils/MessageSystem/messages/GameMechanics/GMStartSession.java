package utils.MessageSystem.messages.GameMechanics;

import base.gameMechanics.GameMechanics;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.util.Set;

public class GMStartSession extends _GameMechanicsMessageTemplate {
    private final Set<Long> userIds;

    public GMStartSession(Address from, Set<Long> userIds) {
        super(from);
        System.out.println("Created GMStartSession");
        this.userIds = userIds;
    }

    @Override
    public void exec(Node node) {
        GameMechanics gameMechanics = null;
        try {
            gameMechanics = (GameMechanics) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (!gameMechanics.hasSession(userIds)) {
            System.out.println("Session started! UserId" + userIds);
            gameMechanics.registerGMSession(userIds);
        }
    }
}
