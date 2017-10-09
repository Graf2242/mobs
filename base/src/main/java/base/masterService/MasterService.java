package base.masterService;

import base.masterService.nodes.Node;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public interface MasterService extends Node, Runnable {
    Map<Class<? extends Node>, List<Socket>> getNodes();

    Connector getConnector();

    void addMessage(Message message);

}
