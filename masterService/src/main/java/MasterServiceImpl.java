package graf.server.MasterService;

import graf.server.Base.Address;
import graf.server.Base.MasterService;
import graf.server.Base.Message;
import graf.server.Base.Node;
import graf.server.Utils.TickSleeper;
import org.omg.CORBA.WrongTransaction;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class MasterServiceImpl implements MasterService {
    final Logger log = Logger.getLogger("MasterService");
    final private Map<Address, Queue<Message>> messages = new HashMap<>();
    final private Map<Class<? extends Node>, List<Address>> nodes = new HashMap<>();
    final private Queue<Message> unsortedMessages = new LinkedBlockingQueue<>();
    private String configPath;

    public MasterServiceImpl(String configPath) {
        this.configPath = configPath;
    }


    public void register(Node node) {
        List<Address> currentAddresses = nodes.get(node.getClass());
        if (currentAddresses == null) {
            currentAddresses = new ArrayList<>();
        }
        currentAddresses.add(node.getAddress());
        nodes.put(node.getClass(), currentAddresses);
    }

    public void addMessage(Message message) {
        unsortedMessages.add(message);
    }

    private void sortMessage(Message message) {
        final Address[] targetL = new Address[1];
        List<Address> allAddressees = new ArrayList<>();
        targetL[0] = null;
        nodes.forEach((aClass, addresses) -> {
            allAddressees.addAll(addresses);
            boolean assignableFrom = message.getTo().isAssignableFrom(aClass);
            if (assignableFrom) {
                targetL[0] = getBestAddressByRole(nodes.get(aClass));
            }
        });

        if (!allAddressees.contains(message.getFrom())) {
            try {
                throw new WrongTransaction("unknown node");
            } catch (WrongTransaction wrongTransaction) {
                log.info("Message from unknown node: " + message.getFrom() + " to: " + message.getTo());
                wrongTransaction.printStackTrace();
            }
        }
        if (targetL[0] == null) {
            try {
                throw new WrongTransaction("unknown node");
            } catch (WrongTransaction wrongTransaction) {
                log.info("Message to unknown node:" + message.getTo() + " from: " + message.toString());
                wrongTransaction.printStackTrace();
            }
        }

        Address target = targetL[0];
        Queue<Message> currentNodeMessages = messages.get(target);
        if (currentNodeMessages == null) {
            currentNodeMessages = new LinkedBlockingQueue<>();
        }
        currentNodeMessages.add(message);
        messages.put(target, currentNodeMessages);
    }

    private Address getBestAddressByRole(List<Address> addresses) {
        return addresses.get(0);
    }

    public void execNodeMessages(Node node) {
        Queue<Message> messages = this.messages.get(node.getAddress());
        if (messages == null) {
            return;
        }
        while (!messages.isEmpty()) {
            Message message = messages.poll();
            message.exec(node);
        }
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            while (!unsortedMessages.isEmpty()) {
                tickSleeper.tickStart();
                Message message = unsortedMessages.poll();
                sortMessage(message);
                tickSleeper.tickEnd();
            }
        }
    }

    @Override
    public Address getAddress() {
        return null;
    }
}
