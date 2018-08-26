import base.databaseService.DBService;
import main.HDBServiceImpl;
import main.MasterServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.logger.LoggerImpl;

public class DBServiceTests {
    private static String configPath = "base/src/main/resources/configs/config.xml";
    private static DBService dbService;

    @BeforeClass
    public static void createBase() {
        new MasterServiceImpl(configPath);
        DBService dbService = new HDBServiceImpl(configPath);
        //noinspection ConstantConditions
        if (dbService instanceof HDBServiceImpl) {
            ((HDBServiceImpl) dbService).setHbm2dll("create");
        }
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.start();
        DBServiceTests.dbService = dbService;
    }

    @Test
    public void createAndGetAccountPositive() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            LoggerImpl.getLogger().error(e);
        }

        dbService.createAccount("acc1", "0");
        dbService.createAccount("acc2", "1");
        dbService.createAccount("acc3", "2");
        dbService.createAccount("acc4", "3");

        Long acc1 = dbService.getAccountId("acc1", "0", 1L);
        Assert.assertNotEquals(acc1, null);
        acc1 = dbService.getAccountId("acc2", "1", 1L);
        Assert.assertNotEquals(acc1, null);
        acc1 = dbService.getAccountId("acc3", "2", 1L);
        Assert.assertNotEquals(acc1, null);
        acc1 = dbService.getAccountId("acc4", "3", 1L);
        Assert.assertNotEquals(acc1, null);
    }

    @Test
    public void getAccountNegative() {
        Assert.assertEquals(null, dbService.getAccountId("notExists", "notExists", 0L));

        dbService.createAccount("acc2-1", "0");

        Assert.assertEquals(null, dbService.getAccountId("acc2-1", "notExists", 0L));
    }

}
