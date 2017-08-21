package main;

import MessageSystem.messages.MessageMasterIsReady;
import MessageSystem.messages.masterService._MasterMessageTemplate;
import ResourceSystem.Resources.configs.ServerConfig;
import Serialization.Serializator;
import databaseService.DBService;
import frontend.Frontend;
import gameMechanics.GameMechanics;
import lobby.Lobby;
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
    final private Queue<Message> unsortedMessages = new LinkedBlockingQueue<>();
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
        new MessageExecutor(nodes, unsortedMessages, sockets, this);

    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;

        MasterService masterService = new MasterServiceImpl(configPath);
        Thread masterThread = new Thread(masterService);
        masterThread.start();
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
        while (!unsortedMessages.isEmpty()) {
            Message message = unsortedMessages.poll();
            log.info("Received message");
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
//                        wrongTransaction.printStackTrace();
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
        while (!allConnected()) {
            tickSleeper.tickStart();
            sortMessages();
            tickSleeper.tickEnd();
        }
        sendAllMasterIsReady();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            sortMessages();
            sendMessages();
            tickSleeper.tickEnd();
        }

    }

    private void sendAllMasterIsReady() {
        unsortedMessages.add(new MessageMasterIsReady(address, Frontend.class));
        unsortedMessages.add(new MessageMasterIsReady(address, DBService.class));
        unsortedMessages.add(new MessageMasterIsReady(address, GameMechanics.class));
        unsortedMessages.add(new MessageMasterIsReady(address, Lobby.class));
        sortMessages();
        sendMessages();
        System.out.println("All Nodes connected!");
    }

    private boolean allConnected() {
        List<Class<? extends Node>> nodes = new ArrayList<>();
        nodes.add(DBService.class);
        nodes.add(Frontend.class);
        nodes.add(GameMechanics.class);
        nodes.add(Lobby.class);
        return this.nodes.keySet().containsAll(nodes);
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
    public void setMasterIsReady(boolean masterIsReady) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket getMasterService() {
        throw new UnsupportedOperationException();
    }
}
