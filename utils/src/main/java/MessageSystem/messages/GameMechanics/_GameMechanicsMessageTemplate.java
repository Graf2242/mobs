package MessageSystem.messages.GameMechanics;

import gameMechanics.GameMechanics;
import masterService.Message;
import masterService.nodes.Address;

public abstract class _GameMechanicsMessageTemplate extends Message {

    public _GameMechanicsMessageTemplate(Address from) {
        super(from, GameMechanics.class);
    }

}
