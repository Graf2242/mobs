import graf.server.Base.DBService;
import graf.server.Base.MasterService;
import graf.server.DBService.HDBServiceImpl;
import graf.server.MasterService.MasterServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBServiceTests {
    static String configPath = "src/test/resources/testConfig";
    private static DBService dbService;

    @BeforeClass
    public static void createBase() {
        MasterService masterService = new MasterServiceImpl(configPath);
        DBService dbService = new HDBServiceImpl(masterService, configPath);
        //noinspection ConstantConditions
        if (dbService instanceof HDBServiceImpl) {
            ((HDBServiceImpl) dbService).setHbm2dll("create-drop");
        }
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.start();
        DBServiceTests.dbService = dbService;
    }

    @Test
    public void getMasterService() {
        MasterService masterService = new MasterServiceImpl(configPath);
        DBService dbService = new HDBServiceImpl(masterService, configPath);
        Assert.assertEquals(masterService, dbService.getMasterService());
    }

    @Test
    public void createAndGetAccountPositive() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dbService.createAccount("acc1", "0");
        dbService.createAccount("acc2", "1");
        dbService.createAccount("acc3", "2");
        dbService.createAccount("acc4", "3");

        Long acc1 = dbService.getAccountId("acc1", "0", 1L);
        Long l = 1L;
        Assert.assertEquals(acc1, l);
        acc1 = dbService.getAccountId("acc2", "1", 1L);
        l = 2L;
        Assert.assertEquals(acc1, l);
        acc1 = dbService.getAccountId("acc3", "2", 1L);
        l = 3L;
        Assert.assertEquals(acc1, l);
        acc1 = dbService.getAccountId("acc4", "3", 1L);
        l = 4L;
        Assert.assertEquals(acc1, l);
    }

    @Test
    public void getAccountNegative() {
        Assert.assertEquals(null, dbService.getAccountId("notExists", "notExists", 0L));

        dbService.createAccount("acc2-1", "0");

        Assert.assertEquals(null, dbService.getAccountId("acc2-1", "notExists", 0L));
    }

}
