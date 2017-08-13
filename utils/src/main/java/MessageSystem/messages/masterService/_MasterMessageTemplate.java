package MessageSystem.messages.masterService;

import masterService.MasterService;
import masterService.Message;
import masterService.nodes.Address;

public abstract class _MasterMessageTemplate extends Message {

    public _MasterMessageTemplate(Address from) {
        super(from, MasterService.class);
    }
}
