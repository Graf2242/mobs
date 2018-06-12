package utils.MessageSystem.messages.masterService;

import base.masterService.MasterService;
import base.masterService.nodes.Address;
import base.utils.Message;

public abstract class _MasterMessageTemplate extends Message {

    public _MasterMessageTemplate(Address from) {
        super(from, MasterService.class);
    }
}
