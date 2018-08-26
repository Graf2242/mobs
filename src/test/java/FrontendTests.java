import base.frontend.Frontend;
import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.MasterService;
import main.FrontendImpl;
import main.FrontendUserSessionImpl;
import main.MasterServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FrontendTests {
    static String configPath = "base/src/main/resources/configs/config.xml";
    private static Frontend frontend;
    private static MasterService masterService;

    @BeforeClass
    public static void createBase() {
        masterService = new MasterServiceImpl(configPath);
        Frontend frontend = new FrontendImpl(configPath);
        //noinspection ConstantConditions
        Thread dbServiceThread = new Thread(frontend);
        dbServiceThread.setName("FE");
        dbServiceThread.start();
        FrontendTests.frontend = frontend;
    }

//    @Test
//    public void addSessionAndUpdateUserId() {
//        FrontendUserSession userSession;
//        userSession = new FrontendUserSessionImpl();
//        frontend.getSessions().put(userSession.getSessionId(), userSession);
//        frontend.updateUserId(userSession.getSessionId(), 100L);
//        Assert.assertEquals(frontend.getSessionBySessionId(1L).getUserId(), (Long) 100L);
//    }


    @Test
    public void updateSessionStatus() {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        userSession.setUserSocket(frontend.getMasterService());
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
