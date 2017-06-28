package graf.server.MasterService.messages.Frontend;

import graf.server.Base.Address;
import graf.server.Base.Frontend;
import graf.server.Base.Message;

public abstract class _FrontendMessageTemplate extends Message {

    public _FrontendMessageTemplate(Address from) {
        super(from, Frontend.class);
    }

}
