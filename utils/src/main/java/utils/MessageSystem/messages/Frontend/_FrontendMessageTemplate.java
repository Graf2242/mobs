package utils.MessageSystem.messages.Frontend;

import base.frontend.Frontend;
import base.masterService.Message;
import base.masterService.nodes.Address;

public abstract class _FrontendMessageTemplate extends Message {

    public _FrontendMessageTemplate(Address from) {
        super(from, Frontend.class);
    }

}
