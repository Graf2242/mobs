package main;

import Mechanics.FightImpl;
import base.gameMechanics.GameMechanics;
import base.gameMechanics.GameMechanicsSession;
import base.masterService.nodes.Address;
import base.utils.Message;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FUpdateSessions;
import utils.MessageSystem.messages.Metrics.MetricsIncrement;
import utils.MessageSystem.messages.masterService.MRegister;
import utils.ResourceSystem.ResourceFactory;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class GameMechanicsImpl implements GameMechanics {
    private static Logger log = LoggerImpl.getLogger("GameMechanicsUDP");
    protected final Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private final NodeMessageReceiver messageReceiver;
    private final ServerConfig serverConfig;
    private Address address = new Address();
    private Set<GameMechanicsSession> sessions = new HashSet<>();
    private Random random = new Random();
    private ResourceFactory resourceFactory;
    private long tickTimeMs = 1000L;

    public GameMechanicsImpl(String configPath) {
        log.fatal("Started");
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();

        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);


        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getMechanics().getIp());
            masterService =
                    new Socket(serverConfig.getMaster().getIp(),
                            Integer.parseInt(serverConfig.getMaster().getMasterPort()),
                            address,
                            Integer.parseInt(serverConfig.getMechanics().getMasterPort()));
        } catch (IOException e) {
            log.error(e);
        }
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, GameMechanics.class, serverConfig.getMechanics().getIp(), serverConfig.getMechanics().getMasterPort()));
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.getLogger("GameMechanics");

        //TODO Вернуть этот GM
        GameMechanics gameMechanics = new GameMechanicsImpl(configPath);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        gameMechanicsThread.start();
    }

    private Socket masterService;
    private String configPath;
    private boolean masterIsReady;

    public Logger getLog() {
        return log;
    }

    public void setTickTimeMs(long tickTimeMs) {
        this.tickTimeMs = tickTimeMs;
    }

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
        tickSleeper.setTickTimeMs(tickTimeMs);
        while (!masterIsReady) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            sendUpdates();
            tickSleeper.tickEnd();
        }
    }

    private void execNodeMessages() {
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    @Override
    public void connectClient(String ip, int port, Long userId) {
        throw new UnsupportedOperationException();
    }

    ;

    protected void sendUpdates() {
        Map<Long, Long> userInfos = new HashMap<>();
        for (GameMechanicsSession session : sessions) {
            for (Long userId : session.getUserIds()) {
                userInfos.put(userId, session.getSessionTime());
            }
        }
        NodeMessageSender.sendMessage(masterService, new FUpdateSessions(address, userInfos));
    }

    @Override
    public void registerGMSession(Set<Long> userIDs) throws UnknownHostException {
        GameMechanicsSession session = new GameMechanicsSessionImpl(userIDs);
        sessions.add(session);
        FightImpl fight = new FightImpl(2, 5, 0, random);
        Thread tFight = new Thread(fight);
        tFight.setName("Fight");
        tFight.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        tFight.start();
        session.setFight(fight);
        log.info("New session registered!");
        NodeMessageSender.sendMessage(masterService, new MetricsIncrement(this.getAddress(), "GMSession", "Count of started sessions"));
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
