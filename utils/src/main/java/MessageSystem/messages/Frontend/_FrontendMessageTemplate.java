package MessageSystem.messages.Frontend;

import frontend.Frontend;
import masterService.Message;
import masterService.nodes.Address;

public abstract class _FrontendMessageTemplate extends Message {

    public _FrontendMessageTemplate(Address from) {
        super(from, Frontend.class);
    }

}
