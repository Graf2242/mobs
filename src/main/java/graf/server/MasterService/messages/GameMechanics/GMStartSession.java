package graf.server.MasterService.messages.GameMechanics;

import graf.server.Base.Address;
import graf.server.Base.GameMechanics;
import graf.server.Base.Node;
import graf.server.GameMechanics.GameMechanicsImpl;

import java.util.Set;

public class GMStartSession extends _GameMechanicsMessageTemplate {
    private final Set<Integer> userIds;

    public GMStartSession(Address from, Set<Integer> userIds) {
        super(from);
        this.userIds = userIds;
    }

    @Override
    public void exec(Node node) {
        GameMechanics gameMechanics = null;
        try {
            gameMechanics = (GameMechanicsImpl) node;
        } catch (Exception ex) {
            System.out.println(node);
            ex.printStackTrace();
        }
        if (!gameMechanics.hasSession(userIds)) {
            gameMechanics.registerGMSession(userIds);
        }
    }
}
