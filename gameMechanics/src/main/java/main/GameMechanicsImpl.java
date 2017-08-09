package main;

import Mechanics.FightImpl;
import ResourceSystem.ResourceFactory;
import gameMechanics.GameMechanics;
import gameMechanics.GameMechanicsSession;
import masterService.Address;
import masterService.MasterService;
import messages.Frontend.FUpdateSessions;
import messages.masterService.MRegister;
import tickSleeper.TickSleeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class GameMechanicsImpl implements GameMechanics {
    private final MasterService masterService;
    private String configPath;
    Address address = new Address();


    public GameMechanicsImpl(MasterService masterService, String configPath) {
        this.masterService = masterService;
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();
    }

    Set<GameMechanicsSession> sessions = new HashSet<>();
    Set<GameMechanicsSession> updatedSessions = new HashSet<>();
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
    public MasterService getMasterService() {
        return masterService;
    }


    @Override
    public void run() {
        masterService.addMessage(new MRegister(address, this));
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
