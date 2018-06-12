package utils.MessageSystem.messages.clientMessages.toClient;

import base.Client.Client;
import base.masterService.nodes.Node;
import base.utils.Message;

public class CConnectViaUDP extends Message {

    private String ip;
    private int port;

    public CConnectViaUDP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void exec(Node node) {
        Client client = (Client) node;
        client.connectViaUDP(ip, port);
    }
}
