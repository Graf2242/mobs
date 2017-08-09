package masterService;

import java.util.List;
import java.util.Map;

public interface MasterService extends Node, Runnable {
    Map<Class<? extends Node>, List<Address>> getNodes();

    String getIpAddress();

    void addMessage(Message message);

    void execNodeMessages(Node node);
}
