package utils.MessageSystem.messages.Lobby;

import base.lobby.Lobby;
import base.masterService.Message;
import base.masterService.nodes.Address;

public abstract class _LobbyMessageTemplate extends Message {

    public _LobbyMessageTemplate(Address from) {
        super(from, Lobby.class);
    }
}
