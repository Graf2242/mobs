package main;

import base.frontend.UserSessionStatus;
import base.lobby.Lobby;
import base.lobby.LobbyUserSession;
import base.masterService.Message;
import base.masterService.nodes.Address;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FUpdateSessions;
import utils.MessageSystem.messages.GameMechanics.GMStartSession;
import utils.MessageSystem.messages.masterService.MRegister;
import utils.ResourceSystem.ResourceFactory;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class LobbyImpl implements Lobby {
    private static Logger log;
    private final Address address = new Address();
    private final NodeMessageReceiver messageReceiver;
    private final Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private final ServerConfig serverConfig;
    private final Integer FIGHT_CAPACITY = 1;
    private final Set<LobbyUserSession> users = new HashSet<>();
    private ResourceFactory resourceFactory;
    private Socket masterService;
    private boolean masterIsReady;

    public LobbyImpl(String configPath) {
        this.resourceFactory = ResourceFactory.instance();

        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getLobby().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getPort()), address,
                    Integer.parseInt(serverConfig.getLobby().getPort()));
        } catch (IOException e) {
            log.error(e);
        }
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, Lobby.class, serverConfig.getLobby().getIp(), serverConfig.getLobby().getPort()));
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.createLogger("Lobby");

        Lobby lobby = new LobbyImpl(configPath);
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.setName("LOBBY");
        lobbyThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        lobbyThread.start();

    }

    public Logger getLog() {
        return log;
    }

    @Override
    public Integer getFIGHT_CAPACITY() {
        return FIGHT_CAPACITY;
    }

    @Override
    public Set<LobbyUserSession> getUsers() {
        return users;
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
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        while (!masterIsReady) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            createFights();
            tickSleeper.tickEnd();
        }
    }

    private void execNodeMessages() {
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    private void createFights() {
        Queue<LobbyUserSession> users = getQueuedUsers();
        while (!users.isEmpty()) {
            Set<Long> fightUsers = new HashSet<>();
            for (int i = 0; i < FIGHT_CAPACITY; i++) {
                LobbyUserSession user = users.poll();
                user.setInFight(true);
                fightUsers.add(user.getUserId());
            }
            NodeMessageSender.sendMessage(masterService, new GMStartSession(address, fightUsers));
        }
    }

    private Queue<LobbyUserSession> getQueuedUsers() {
        Queue<LobbyUserSession> queuedUsers = new LinkedBlockingQueue<>();
        for (LobbyUserSession user : users) {
            if (!user.isInFight()) {
                queuedUsers.add(user);
            }
        }
        return queuedUsers;
    }

    @Override
    public void registerUser(LobbyUserSession userInfo) {
        users.add(userInfo);

    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }

    @Override
    public void registerIfExists(Long userId, String userName) {
        LobbyUserSession lobbyUserInfo = new LobbyUserSessionImpl(userId, userName);
        if (!getUsers().contains(lobbyUserInfo)) {
            registerUser(lobbyUserInfo);
            NodeMessageSender.sendMessage(masterService, new FUpdateSessions(getAddress(), userId, UserSessionStatus.LOBBY));
        }
    }
}
