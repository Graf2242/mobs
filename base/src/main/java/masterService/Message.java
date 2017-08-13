package masterService;

import masterService.nodes.Address;
import masterService.nodes.Node;

import java.io.Serializable;

public abstract class Message implements Serializable {
    final private Address from;
    final private Class<? extends Node> to;

    public Message(Address from, Class<? extends Node> to) {
        this.from = from;
        this.to = to;
    }

    public Message() {
        from = null;
        to = null;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public Address getFrom() {
        return from;
    }

    public Class<? extends Node> getTo() {
        return to;
    }

    public abstract void exec(Node node);


}
