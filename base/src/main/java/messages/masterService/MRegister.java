package messages.masterService;

import masterService.Address;
import masterService.MasterService;
import masterService.Node;

import java.util.ArrayList;
import java.util.List;

public class MRegister extends _MasterMessageTemplate {

    private final Node node;

    public MRegister(Address from, Node node) {
        super(from);
        this.node = node;
    }

    @Override
    public void exec(Node node) {
        MasterService masterService = (MasterService) node;
        List<Address> currentAddresses = masterService.getNodes().get(this.node.getClass());
        if (currentAddresses == null) {
            currentAddresses = new ArrayList<>();
        }
        currentAddresses.add(this.node.getAddress());
        masterService.getNodes().put(this.node.getClass(), currentAddresses);
    }
}
