package main;

import Mechanics.FightImpl;
import MessageSystem.NodeMessageReceiver;
import MessageSystem.NodeMessageSender;
import MessageSystem.messages.Frontend.FUpdateSessions;
import MessageSystem.messages.masterService.MRegister;
import ResourceSystem.ResourceFactory;
import ResourceSystem.Resources.configs.ServerConfig;
import gameMechanics.GameMechanics;
import gameMechanics.GameMechanicsSession;
import masterService.Message;
import masterService.nodes.Address;
import tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class GameMechanicsImpl implements GameMechanics {
    private final Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private final NodeMessageReceiver messageReceiver;
    private final ServerConfig serverConfig;
    private Socket masterService;
    private String configPath;
    Address address = new Address();
    private boolean masterIsReady;

    public GameMechanicsImpl(String configPath) {
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();

        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);


        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getMechanics().getIp());
            masterService =
                    new Socket(serverConfig.getMaster().getIp(),
                            Integer.parseInt(serverConfig.getMaster().getPort()),
                            address,
                            Integer.parseInt(serverConfig.getMechanics().getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService, this);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, GameMechanics.class, serverConfig.getMechanics().getIp(), serverConfig.getMechanics().getPort()));
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        GameMechanics gameMechanics = new GameMechanicsImpl(configPath);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();
    }

    Set<GameMechanicsSession> sessions = new HashSet<>();
    Random random = new Random();
    Logger log = Logger.getLogger("GameMechanics");
    ResourceFactory resourceFactory;

    @Override
    public Set<GameMechanicsSession> getSessions() {
        return sessions;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setMasterIsReady(boolean masterIsReady) {
        this.masterIsReady = true;
    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(1000L);
        while (!masterIsReady) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            sendUpdatesToFrontend();
            tickSleeper.tickEnd();
        }
    }

    private void execNodeMessages() {
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    private void sendUpdatesToFrontend() {
        Map<Long, Long> userInfos = new HashMap<>();
        for (GameMechanicsSession session : sessions) {
            for (Long userId : session.getUserIds()) {
                userInfos.put(userId, session.getSessionTime());
            }
        }
        NodeMessageSender.sendMessage(masterService, new FUpdateSessions(address, userInfos));
    }

    @Override
    public void registerGMSession(Set<Long> userIDs) {
        GameMechanicsSession session = new GameMechanicsSessionImpl(userIDs);
        sessions.add(session);
        FightImpl fight = new FightImpl(2, 5, 0, random);
        Thread tFight = new Thread(fight);
        tFight.setName("Fight");
        tFight.start();
        session.setFight(fight);
        log.info("New session registered!");
    }

    @Override
    public boolean hasSession(Set<Long> userIds) {
        for (GameMechanicsSession session : sessions) {
            for (Long userId : userIds) {
                if (session.getUserIds().contains(userId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
