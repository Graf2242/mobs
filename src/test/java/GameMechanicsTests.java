import gameMechanics.GameMechanics;
import gameMechanics.GameMechanicsSession;
import main.GameMechanicsImpl;
import main.GameMechanicsSessionImpl;
import main.MasterServiceImpl;
import masterService.MasterService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GameMechanicsTests {
    static String configPath = "src/test/resources/testConfig";
    private static GameMechanics gameMechanics;
    private static MasterService masterService;

    @BeforeClass
    public static void createBase() {
        masterService = new MasterServiceImpl(configPath);
        gameMechanics = new GameMechanicsImpl(masterService, configPath);
        //noinspection ConstantConditions
        Thread dbServiceThread = new Thread(gameMechanics);
        dbServiceThread.setName("GM");
        dbServiceThread.start();
    }

    @Test
    public void getMasterService() {
        MasterService masterService = new MasterServiceImpl(configPath);
        GameMechanics gameMechanics = new GameMechanicsImpl(masterService, configPath);
        Assert.assertEquals(gameMechanics.getMasterService(), masterService);
    }

    @Test
    public void registerGMSession() {
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userIds.add(2L);
        userIds.add(3L);
        gameMechanics.registerGMSession(userIds);
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
        gameMechanics.registerGMSession(userIds);
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
            e.printStackTrace();
        }
        Long sessionTime = gms.getSessionTime();
        boolean condition = Math.abs(sessionTime - 1000L) < 50L;
        boolean isStart1000ms = (new Date().getTime() - sessionTime) - gms.getStartTime() < 50;
        Assert.assertTrue(condition);
        Assert.assertTrue(isStart1000ms);

    }
}
