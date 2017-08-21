package MessageSystem.messages;

import masterService.Message;
import masterService.nodes.Address;
import masterService.nodes.Node;

public class MessageMasterIsReady extends Message {


    public MessageMasterIsReady(Address from, Class<? extends Node> to) {
        super(from, to);
    }

    @Override
    public void exec(Node node) {
        node.setMasterIsReady(true);
    }
}
