package main;

import MessageSystem.NodeMessageReceiver;
import MessageSystem.NodeMessageSender;
import MessageSystem.messages.Frontend.FUpdateSessions;
import MessageSystem.messages.GameMechanics.GMStartSession;
import MessageSystem.messages.masterService.MRegister;
import ResourceSystem.ResourceFactory;
import ResourceSystem.Resources.configs.ServerConfig;
import frontend.UserSessionStatus;
import lobby.Lobby;
import lobby.LobbyUserSession;
import masterService.Message;
import masterService.nodes.Address;
import tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class LobbyImpl implements Lobby {
    private final Address address = new Address();
    private final NodeMessageReceiver messageReceiver;
    private final Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private final ServerConfig serverConfig;
    private final Integer FIGHT_CAPACITY = 1;
    private final Set<LobbyUserSession> users = new HashSet<>();
    private ResourceFactory resourceFactory;
    private Socket masterService;

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
            e.printStackTrace();
        }
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService, this);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, this, serverConfig.getLobby().getIp(), serverConfig.getLobby().getPort()));
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
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
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
