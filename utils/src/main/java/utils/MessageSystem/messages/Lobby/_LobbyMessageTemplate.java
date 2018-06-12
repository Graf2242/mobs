package utils.MessageSystem.messages.Lobby;

import base.lobby.Lobby;
import base.masterService.nodes.Address;
import base.utils.Message;

public abstract class _LobbyMessageTemplate extends Message {

    public _LobbyMessageTemplate(Address from) {
        super(from, Lobby.class);
    }
}
