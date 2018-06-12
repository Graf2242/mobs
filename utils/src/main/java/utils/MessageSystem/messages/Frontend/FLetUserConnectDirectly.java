package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.util.Set;

public class FLetUserConnectDirectly extends _FrontendMessageTemplate {

    private Set<Long> userIds;
    private String ip;
    private int port;

    public FLetUserConnectDirectly(Address from, Set<Long> userIds, String ip, int port) {
        super(from);
        this.userIds = userIds;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void exec(Node node) {
        Frontend frontend = (Frontend) node;
        frontend.redirectUsersToMechanics(userIds, ip, port);
    }
}
