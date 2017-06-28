package graf.server.MasterService.messages.AccountServer;

import graf.server.Base.AccountService;
import graf.server.Base.Address;
import graf.server.Base.Message;

public abstract class _AccountServiceMessageTemplate extends Message {

    public _AccountServiceMessageTemplate(Address from) {
        super(from, AccountService.class);
    }

}
