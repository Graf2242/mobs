package graf.server.GameMechanics;

import graf.server.Base.Address;
import graf.server.Base.GameMechanics;
import graf.server.Base.MasterService;
import graf.server.GameMechanics.Mechanics.Fight;
import graf.server.MasterService.messages.Frontend.FUpdateSessions;
import graf.server.Utils.ResourceSystem.ResourceFactory;
import graf.server.Utils.TickSleeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class GameMechanicsImpl implements GameMechanics {
    private final MasterService masterService;
    Address address = new Address();
    Set<GameMechanicsSession> sessions = new HashSet<>();
    Set<GameMechanicsSession> updatedSessions = new HashSet<>();
    Random random = new Random();
    Logger log = Logger.getLogger("GameMechanics");


    public GameMechanicsImpl(MasterService masterService, ResourceFactory resourceFactory) {
        this.masterService = masterService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public MasterService getMasterService() {
        return masterService;
    }

    @Override

    public void run() {
        getMasterService().register(this);
        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(100L);
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            sendUpdatesToFrontend();
            tickSleeper.tickEnd();
        }
    }

    private void sendUpdatesToFrontend() {
        masterService.addMessage(new FUpdateSessions(address, sessions));
    }

    public void registerGMSession(Set<Integer> userIDs) {
        GameMechanicsSession session = new GameMechanicsSession(userIDs);
        sessions.add(session);
        log.info("New session registered!");
        session.setFight(new Fight(2, 5, 0, random));
    }

    @Override
    public boolean hasSession(Set<Integer> userIds) {
        for (GameMechanicsSession session : sessions) {
            if (session.getUserIds().equals(userIds)) {
                return true;
            }
        }
        return false;
    }
}
