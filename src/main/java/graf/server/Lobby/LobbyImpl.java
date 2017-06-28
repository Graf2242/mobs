package graf.server.Lobby;

import graf.server.Base.Address;
import graf.server.Base.Lobby;
import graf.server.Base.MasterService;
import graf.server.MasterService.messages.GameMechanics.GMStartSession;
import graf.server.Utils.TickSleeper;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class LobbyImpl implements Lobby {
    private final Address address = new Address();
    private final MasterService masterService;
    private final Integer FIGHT_CAPACITY = 1;
    private final Set<LobbyUserSession> users = new HashSet<>();

    public LobbyImpl(MasterService masterService) {
        this.masterService = masterService;
    }

    public Integer getFIGHT_CAPACITY() {
        return FIGHT_CAPACITY;
    }

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
            Set<Integer> fightUsers = new HashSet<>();
            for (int i = 0; i < FIGHT_CAPACITY; i++) {
                fightUsers.add(users.poll().getUserId());
            }
            masterService.addMessage(new GMStartSession(address, fightUsers));
        }
    }

    private Queue<LobbyUserSession> getQueuedUsers() {
        Queue<LobbyUserSession> queuedUsers = new LinkedBlockingQueue<>();
        for (LobbyUserSession user : users) {
            if (!user.isInFight) {
                queuedUsers.add(user);
            }
        }
        return queuedUsers;
    }

    public void registerUser(LobbyUserSession userInfo) {
        users.add(userInfo);

    }

    public MasterService getMasterService() {
        return masterService;
    }
}
