package utils.MessageSystem.messages;

import base.masterService.Message;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

public class MessageMasterIsReady extends Message {


    public MessageMasterIsReady(Address from, Class<? extends Node> to) {
        super(from, to);
    }

    @Override
    public void exec(Node node) {
        node.setMasterIsReady(true);
    }
}
