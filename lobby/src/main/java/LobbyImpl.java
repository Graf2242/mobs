import ResourceSystem.ResourceFactory;
import frontend.UserSessionStatus;
import graf.server.Base.Address;
import graf.server.Base.MasterService;
import graf.server.Utils.TickSleeper;
import lobby.Lobby;
import lobby.LobbyUserSession;
import messages.GameMechanics.GMStartSession;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class LobbyImpl implements Lobby {
    private final Address address = new Address();
    private final MasterService masterService;
    private String configPath;
    private final Integer FIGHT_CAPACITY = 1;
    private final Set<LobbyUserSession> users = new HashSet<>();
    private ResourceFactory resourceFactory;

    public LobbyImpl(MasterService masterService, String configPath) {
        this.masterService = masterService;
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();
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
        masterService.register(this);
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            createFights();
            tickSleeper.tickEnd();
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
            masterService.addMessage(new GMStartSession(address, fightUsers));
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
    public MasterService getMasterService() {
        return masterService;
    }

    @Override
    public void registerIfExists(Long userId, String userName) {
        LobbyUserSession lobbyUserInfo = new LobbyUserSessionImpl(userId, userName);
        if (!getUsers().contains(lobbyUserInfo)) {
            registerUser(lobbyUserInfo);
            getMasterService().addMessage(new FUpdateSessions(getAddress(), userId, UserSessionStatus.LOBBY));
        }
    }
}
