package graf.server.Base;

public abstract class Message {
    @Override
    public String toString() {
        return from.toString();
    }

    final private graf.server.Base.Address from;
    final private Class<? extends graf.server.Base.Node> to;

    public Message(graf.server.Base.Address from, Class<? extends graf.server.Base.Node> to) {
        this.from = from;
        this.to = to;
    }

    public graf.server.Base.Address getFrom() {
        return from;
    }

    public Class<? extends graf.server.Base.Node> getTo() {
        return to;
    }

    public abstract void exec(graf.server.Base.Node node);
}
