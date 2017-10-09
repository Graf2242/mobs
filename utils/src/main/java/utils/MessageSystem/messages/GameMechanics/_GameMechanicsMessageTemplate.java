package utils.MessageSystem.messages.GameMechanics;

import base.gameMechanics.GameMechanics;
import base.masterService.Message;
import base.masterService.nodes.Address;

public abstract class _GameMechanicsMessageTemplate extends Message {

    public _GameMechanicsMessageTemplate(Address from) {
        super(from, GameMechanics.class);
    }

}
