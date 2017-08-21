package MessageSystem.messages.Lobby;

import lobby.Lobby;
import masterService.Message;
import masterService.nodes.Address;

public abstract class _LobbyMessageTemplate extends Message {

    public _LobbyMessageTemplate(Address from) {
        super(from, Lobby.class);
    }
}
