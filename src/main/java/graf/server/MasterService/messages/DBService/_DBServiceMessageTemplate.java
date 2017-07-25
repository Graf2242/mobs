package graf.server.MasterService.messages.DBService;

import graf.server.Base.Address;
import graf.server.Base.DBService;
import graf.server.Base.Message;

public abstract class _DBServiceMessageTemplate extends Message {

    public _DBServiceMessageTemplate(Address from) {
        super(from, DBService.class);
    }

}
