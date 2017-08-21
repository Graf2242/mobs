package MessageSystem.messages.DBService;


import databaseService.DBService;
import masterService.Message;
import masterService.nodes.Address;

public abstract class _DBServiceMessageTemplate extends Message {

    public _DBServiceMessageTemplate(Address from) {
        super(from, DBService.class);
    }

}
