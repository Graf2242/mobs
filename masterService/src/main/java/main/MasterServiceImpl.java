package main;

import masterService.Address;
import masterService.MasterService;
import masterService.Message;
import masterService.Node;
import messages.masterService.MRegister;
import org.omg.CORBA.WrongTransaction;
import tickSleeper.TickSleeper;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;


public class MasterServiceImpl implements MasterService {
    final Logger log = Logger.getLogger("MasterService");
    final private Map<Address, Queue<Message>> messages = new HashMap<>();
    Address address = new Address();
    private String ipAddress;

    final private Map<Class<? extends Node>, List<Address>> nodes = new HashMap<>();

    @Override
    public Map<Class<? extends Node>, List<Address>> getNodes() {
        return nodes;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }
    final private Queue<Message> unsortedMessages = new LinkedBlockingQueue<>();
    private String configPath;

    public MasterServiceImpl(String configPath) {
        this.configPath = configPath;
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

        if (!(message instanceof MRegister)) {
            if (!allAddressees.contains(message.getFrom()) && !message.getFrom().equals(address)) {
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
                execNodeMessages(this);
                tickSleeper.tickEnd();
            }
        }
    }

    @Override
    public Address getAddress() {
        return null;
    }

    @Override
    public MasterService getMasterService() {
        return this;
    }
}
