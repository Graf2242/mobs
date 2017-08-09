package messages.masterService;

import lobby.Lobby;
import masterService.Address;
import masterService.Message;

public abstract class _MasterMessageTemplate extends Message {

    public _MasterMessageTemplate(Address from) {
        super(from, Lobby.class);
    }
}
