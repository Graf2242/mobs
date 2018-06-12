package utils.MessageSystem.messages.GameMechanics;

import base.gameMechanics.GameMechanics;
import base.masterService.nodes.Address;
import base.utils.Message;

public abstract class _GameMechanicsMessageTemplate extends Message {

    public _GameMechanicsMessageTemplate(Address from) {
        super(from, GameMechanics.class);
    }

}
