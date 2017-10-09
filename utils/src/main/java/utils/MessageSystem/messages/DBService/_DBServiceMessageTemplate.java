package utils.MessageSystem.messages.DBService;


import base.databaseService.DBService;
import base.masterService.Message;
import base.masterService.nodes.Address;

public abstract class _DBServiceMessageTemplate extends Message {

    public _DBServiceMessageTemplate(Address from) {
        super(from, DBService.class);
    }

}
