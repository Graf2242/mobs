package utils.MessageSystem.messages.GameMechanics;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

public class GMConnectUDPClient extends _GameMechanicsMessageTemplate {
    private Long userId;

    public GMConnectUDPClient(Address from, Long userId) {
        super(from);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public void exec(Node node) {
    }
}
