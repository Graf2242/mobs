package graf.server.Base;

public abstract class Message {
    @Override
    public String toString() {
        return from.toString();
    }

    final private Address from;
    final private Class<? extends Node> to;

    public Message(Address from, Class<? extends Node> to) {
        this.from = from;
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Class<? extends Node> getTo() {
        return to;
    }

    public abstract void exec(Node node);
}
