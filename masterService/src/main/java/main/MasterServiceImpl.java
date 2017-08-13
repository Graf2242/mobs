package main;

import MessageSystem.messages.masterService.MRegister;
import MessageSystem.messages.masterService._MasterMessageTemplate;
import ResourceSystem.Resources.configs.ServerConfig;
import Serialization.Serializator;
import masterService.Connector;
import masterService.MasterService;
import masterService.Message;
import masterService.nodes.Address;
import masterService.nodes.Node;
import org.omg.CORBA.WrongTransaction;
import tickSleeper.TickSleeper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;


public class MasterServiceImpl implements MasterService {
    final Logger log = Logger.getLogger("MasterService");
    final private Map<Socket, Queue<Message>> messages = new HashMap<>();
    Address address = new Address();
    final private Map<Class<? extends Node>, List<Socket>> nodes = new HashMap<>();
    private final Connector connector;
    final private List<Message> unsortedMessages = new CopyOnWriteArrayList<>();
    ServerConfig serverConfig;

    private String configPath;

    public MasterServiceImpl(String configPath) {
        this.configPath = configPath;
        serverConfig = ServerConfig.newInstance(configPath);

        InetAddress inetAddress;
        ServerSocket serverSocket = null;
        try {
            if (serverConfig != null) {
                inetAddress = Inet4Address.getByName(serverConfig.getMaster().getIp());
                serverSocket = new ServerSocket(Integer.parseInt(serverConfig.getMaster().getPort()), 10, inetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Socket> sockets = new CopyOnWriteArrayList<>();
        connector = new ConnectorImpl(serverSocket, sockets);
        new MessageExecutor(nodes, unsortedMessages, sockets);

    }

    public static void main(String[] args) {
        MasterService masterService = new MasterServiceImpl(args[0]);
        masterService.run();
    }

    @Override
    public Map<Class<? extends Node>, List<Socket>> getNodes() {
        return nodes;
    }

    public void addMessage(Message message) {
        unsortedMessages.add(message);
    }

    @Override
    public Connector getConnector() {
        return connector;
    }

    private void sortMessages() {
        for (Message message : unsortedMessages) {
            unsortedMessages.remove(message);
            if (Objects.equals(message, null)) {
                continue;
            }
            if (!(message instanceof _MasterMessageTemplate)) {
                final Socket[] targetL = new Socket[1];
                targetL[0] = null;
                nodes.forEach((aClass, addresses) -> {
                    boolean assignableFrom = message.getTo().isAssignableFrom(aClass);
                    if (assignableFrom) {
                        targetL[0] = getBestAddressByRole(nodes.get(aClass));
                    }
                });
                if (targetL[0] == null) {
                    try {
                        throw new WrongTransaction("unknown node");
                    } catch (WrongTransaction wrongTransaction) {
                        log.info("Message to unknown node:" + message.getTo() + " from: " + message.toString());
                        wrongTransaction.printStackTrace();
                    }
                }
                Socket target = targetL[0];
                Queue<Message> currentNodeMessages = messages.get(target);
                if (currentNodeMessages == null) {
                    currentNodeMessages = new LinkedBlockingQueue<>();
                }
                currentNodeMessages.add(message);
                messages.put(target, currentNodeMessages);
            } else {
                message.exec(this);
            }
        }
    }

    @Deprecated
    private void sortMessageOld(Message message) {
        final Socket[] targetL = new Socket[1];
        List<Socket> allAddressees = new ArrayList<>();
        targetL[0] = null;
        nodes.forEach((aClass, addresses) -> {
            allAddressees.addAll(addresses);
            boolean assignableFrom = message.getTo().isAssignableFrom(aClass);
            if (assignableFrom) {
                targetL[0] = getBestAddressByRole(nodes.get(aClass));
            }
        });

        if (!(message instanceof MRegister)) {
            if (targetL[0] == null) {
                try {
                    throw new WrongTransaction("unknown node");
                } catch (WrongTransaction wrongTransaction) {
                    log.info("Message to unknown node:" + message.getTo() + " from: " + message.toString());
                    wrongTransaction.printStackTrace();
                }
            }
        }

        Socket target = targetL[0];
        Queue<Message> currentNodeMessages = messages.get(target);
        if (currentNodeMessages == null) {
            currentNodeMessages = new LinkedBlockingQueue<>();
        }
        currentNodeMessages.add(message);
        messages.put(target, currentNodeMessages);
    }

    private Socket getBestAddressByRole(List<Socket> addresses) {
        return addresses.get(0);
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            sortMessages();
            sendMessages();
            tickSleeper.tickEnd();
        }

    }

    private void sendMessages() {
        nodes.forEach((aClass, sockets) -> {
            for (Socket socket : sockets) {
                DataOutputStream dos = null;
                Queue<Message> messages = this.messages.get(socket);
                if (Objects.equals(messages, null)) {
                    continue;
                }
                for (Message message : messages) {
                    messages.remove(message);
                    try {
                        dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeUTF(Serializator.serializeToString(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dos != null) {
                    try {
                        dos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public Address getAddress() {
        return null;
    }

    @Override
    public Socket getMasterService() {
        throw new UnsupportedOperationException();
    }
}
