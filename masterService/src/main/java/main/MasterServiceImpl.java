package main;

import base.databaseService.DBService;
import base.frontend.Frontend;
import base.gameMechanics.GameMechanics;
import base.lobby.Lobby;
import base.masterService.Connector;
import base.masterService.MasterService;
import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import base.utils.Message;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.WrongTransaction;
import utils.MessageSystem.messages.MessageMasterIsReady;
import utils.MessageSystem.messages.masterService._MasterMessageTemplate;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.Serialization.SerializerHelper;
import utils.ServerSocketUtils.ConnectorImpl;
import utils.ServerSocketUtils.MessageExecutor;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class MasterServiceImpl implements MasterService {


    private static Logger log;
    final private Map<Socket, Queue<Message>> messages = new HashMap<>();
    final private Map<Class<? extends Node>, List<Socket>> nodes = new HashMap<>();
    private final Connector connector;
    final private Queue<Message> unsortedMessages = new LinkedBlockingQueue<>();
    private Address address = new Address();
    private ServerConfig serverConfig;
    private String configPath;


    public MasterServiceImpl(String configPath) {
        this.configPath = configPath;
        serverConfig = ServerConfig.newInstance(configPath);
        List<Socket> sockets = new CopyOnWriteArrayList<>();
        if (serverConfig == null) log.fatal(new RuntimeException("Server config is null"));
        connector = new ConnectorImpl(serverConfig.getMaster().getIp(), serverConfig.getMaster().getMasterPort(), sockets, log);
        new MessageExecutor(unsortedMessages, sockets, log);
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.getLogger("MasterService");

        MasterService masterService = new MasterServiceImpl(configPath);
        Thread masterThread = new Thread(masterService);
        masterThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        masterThread.start();
    }

    public Logger getLog() {
        return log;
    }

    @Override
    public Map<Class<? extends Node>, List<Socket>> getNodes() {
        return nodes;
    }

    @Override
    public void addMessage(Message message) {
        unsortedMessages.add(message);
    }

    @Override
    public Connector getConnector() {
        return connector;
    }

    @Override
    public void sortMessages() {
        while (!unsortedMessages.isEmpty()) {
            Message message = unsortedMessages.poll();
            log.trace("Received message");
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

    private Socket getBestAddressByRole(List<Socket> addresses) {
        return addresses.get(0);
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e);
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
        log.info("All Nodes connected!");
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
                        dos.writeUTF(SerializerHelper.serializeToString(message));
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
                if (dos != null) {
                    try {
                        dos.flush();
                    } catch (IOException e) {
                        log.error(e);
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
