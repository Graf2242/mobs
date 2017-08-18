package MessageSystem.messages.masterService;

import masterService.MasterService;
import masterService.nodes.Address;
import masterService.nodes.Node;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MRegister extends _MasterMessageTemplate {


    private final Class<? extends Node> node;
    private String ipFrom;
    private String portFrom;

    public MRegister(Address from, Node node, String ipFrom, String portFrom) {
        super(from);
        this.node = node.getClass();
        this.ipFrom = ipFrom;
        this.portFrom = portFrom;
    }

    @Override
    public void exec(Node node) {
        MasterService masterService = (MasterService) node;
        Class<? extends Node> nodeClass = this.node;
        List<Socket> currentAddresses = masterService.getNodes().get(nodeClass);
        if (currentAddresses == null) {
            currentAddresses = new ArrayList<>();
        }
        Socket socketR = null;
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ipFrom);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (Socket socket : masterService.getConnector().getSockets()) {
            if (socket.getInetAddress().equals(address) &&
                    (Objects.equals(socket.getPort(), Integer.parseInt(this.portFrom)))) {
                socketR = socket;
            }
        }
        if (!currentAddresses.contains(socketR)) {
            currentAddresses.add(socketR);
            masterService.getNodes().put(nodeClass, currentAddresses);
        }
    }
}