package graf.server.MasterService.messages.GameMechanics;

import graf.server.Base.Address;
import graf.server.Base.GameMechanics;
import graf.server.Base.Message;

public abstract class _GameMechanicsMessageTemplate extends Message {

    public _GameMechanicsMessageTemplate(Address from) {
        super(from, GameMechanics.class);
    }

}
