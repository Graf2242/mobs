package graf.server.MasterService.messages.Lobby;

import graf.server.Base.Address;
import graf.server.Base.Lobby;
import graf.server.Base.Message;

public abstract class _LobbyMessageTemplate extends Message {

    public _LobbyMessageTemplate(Address from) {
        super(from, Lobby.class);
    }
}
