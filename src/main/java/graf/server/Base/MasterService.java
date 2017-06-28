package graf.server.Base;

public interface MasterService extends Node, Runnable {
    void register(Node node);

    void addMessage(Message message);

    void execNodeMessages(Node node);
}
