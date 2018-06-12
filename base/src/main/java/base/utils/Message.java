package base.utils;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;

import java.io.Serializable;
import java.net.Socket;

public abstract class Message implements Serializable {
    final private Address from;
    final private Class<? extends Node> to;
    protected transient Socket outSocket;

    public Message(Address from, Class<? extends Node> to) {
        this.from = from;
        this.to = to;
    }

    public Message() {
        from = null;
        to = null;
    }

    public void setOutSocket(Socket outSocket) {
        this.outSocket = outSocket;
    }

    public Address getFrom() {
        return from;
    }

    public Class<? extends Node> getTo() {
        return to;
    }

    public abstract void exec(Node node);


}
