import base.gameMechanics.GameMechanics;
import base.gameMechanics.GameMechanicsSession;
import base.masterService.MasterService;
import main.GameMechanicsImpl;
import main.GameMechanicsSessionImpl;
import main.MasterServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.logger.LoggerImpl;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GameMechanicsTests {
    private static String configPath = "base/src/main/resources/configs/config.xml";
    private static GameMechanics gameMechanics;
    private static MasterService masterService;

    @BeforeClass
    public static void createBase() {
        masterService = new MasterServiceImpl(configPath);
        gameMechanics = new GameMechanicsImpl(configPath);
        //noinspection ConstantConditions
        Thread dbServiceThread = new Thread(gameMechanics);
        dbServiceThread.setName("GM");
        dbServiceThread.start();
    }

    @Test
    public void registerGMSession() {
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userIds.add(2L);
        userIds.add(3L);
        try {
            gameMechanics.registerGMSession(userIds);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (GameMechanicsSession gameMechanicsSession : gameMechanics.getSessions()) {
            if (gameMechanicsSession.getUserIds().equals(userIds)) {
                return;
            }
        }
        Assert.assertEquals(1L, 0L);
    }

    @Test
    public void hasSession() {
        Set<Long> userIds = new HashSet<>();
        userIds.add(4L);
        userIds.add(5L);
        userIds.add(6L);
        Set<Long> idsToCheck = new HashSet<>();
        idsToCheck.add(-1L);
        try {
            gameMechanics.registerGMSession(userIds);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(gameMechanics.hasSession(idsToCheck), false);
        idsToCheck.add(5L);
        Assert.assertEquals(gameMechanics.hasSession(idsToCheck), true);
    }

    @Test
    public void sessionGetTime() {
        Set<Long> ids = new HashSet<>();
        GameMechanicsSession gms = new GameMechanicsSessionImpl(ids);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            LoggerImpl.getLogger().error(e);
        }
        Long sessionTime = gms.getSessionTime();
        boolean condition = Math.abs(sessionTime - 1000L) < 100L;
        boolean isStart1000ms = (new Date().getTime() - sessionTime) - gms.getStartTime() < 50;
        Assert.assertTrue(condition);
        Assert.assertTrue(isStart1000ms);

    }
}
