import frontend.Frontend;
import frontend.FrontendUserSession;
import frontend.UserSessionStatus;
import main.FrontendImpl;
import main.FrontendUserSessionImpl;
import main.MasterServiceImpl;
import masterService.MasterService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FrontendTests {
    static String configPath = "src/test/resources/testConfig";
    private static Frontend frontend;
    private static MasterService masterService;

    @BeforeClass
    public static void createBase() {
        masterService = new MasterServiceImpl(configPath);
        Frontend frontend = new FrontendImpl(masterService, configPath);
        //noinspection ConstantConditions
        Thread dbServiceThread = new Thread(frontend);
        dbServiceThread.setName("FE");
        dbServiceThread.start();
        FrontendTests.frontend = frontend;
    }

    @Test
    public void getMasterService() {
        MasterService masterService = new MasterServiceImpl(configPath);
        Frontend frontend = new FrontendImpl(masterService, configPath);
        Assert.assertEquals(frontend.getMasterService(), masterService);
    }

    @Test
    public void addSessionAndUpdateUserId() {
        FrontendUserSession userSession;
        userSession = new FrontendUserSessionImpl();
        frontend.getSessions().put(userSession.getSessionId(), userSession);
        frontend.updateUserId(userSession.getSessionId(), 100L);
        Assert.assertEquals(frontend.getSessionBySessionId(0L).getUserId(), (Long) 100L);
    }


    @Test
    public void updateSessionStatus() {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        frontend.getSessions().put(userSession.getSessionId(), userSession);
        frontend.updateSessionStatus(userSession.getSessionId(), UserSessionStatus.CONNECTED);
        Assert.assertEquals(UserSessionStatus.CONNECTED, frontend.getSessionBySessionId(userSession.getSessionId()).getStatus());
        frontend.updateSessionStatus(userSession.getSessionId(), UserSessionStatus.FIGHT);
        Assert.assertEquals(UserSessionStatus.FIGHT, frontend.getSessionBySessionId(userSession.getSessionId()).getStatus());
    }

    @Test
    public void getSessionId() {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        userSession.setUserName("qwerty");
        frontend.getSessions().put(userSession.getSessionId(), userSession);
        Assert.assertEquals(frontend.getSessionId("qwerty"), userSession.getSessionId());
    }

    @Test
    public void setSessionBySessionId() {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        frontend.getSessions().put(userSession.getSessionId(), userSession);
        Assert.assertEquals(frontend.getSessionBySessionId(userSession.getSessionId()), userSession);
    }

    @Test
    public void getSessionByUserId() {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        userSession.setUserId(123L);
        frontend.getSessions().put(userSession.getSessionId(), userSession);
        Assert.assertEquals(frontend.getSessionByUserId(123L), userSession);

    }
}
