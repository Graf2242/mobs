import graf.server.Base.Lobby;
import graf.server.Base.MasterService;
import graf.server.Lobby.LobbyImpl;
import graf.server.Lobby.LobbyUserSession;
import graf.server.MasterService.MasterServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class LobbyTests {
    static String configPath = "src/test/resources/testConfig";
    private static Lobby lobby;
    private static MasterService masterService;

    @BeforeClass
    public static void create() {
        masterService = new MasterServiceImpl(configPath);
        lobby = new LobbyImpl(masterService, configPath);
        //noinspection ConstantConditions
        Thread dbServiceThread = new Thread(lobby);
        dbServiceThread.setName("Lobby");
        dbServiceThread.start();
    }

    @Test
    public void getMasterService() {
        MasterService masterService = new MasterServiceImpl(configPath);
        Lobby lobby = new LobbyImpl(masterService, configPath);
        Assert.assertEquals(lobby.getMasterService(), masterService);
    }

    @Test
    public void getCapacity() throws NoSuchFieldException, IllegalAccessException {
        Assert.assertEquals(lobby.getFIGHT_CAPACITY().intValue(), 1);
        Assert.assertNotEquals(lobby.getFIGHT_CAPACITY().intValue(), 2);
    }

    @Test
    public void getUsers() throws IllegalAccessException, NoSuchFieldException {
        Set<LobbyUserSession> userSessions = new HashSet<>();
        userSessions.add(new LobbyUserSession(1L, "123"));
        userSessions.add(new LobbyUserSession(2L, "234"));
        Field field;
        //noinspection JavaReflectionMemberAccess
        field = lobby.getClass().getDeclaredField("users");
        field.setAccessible(true);
        field.set(lobby, userSessions);

        Assert.assertEquals(lobby.getUsers(), userSessions);

        field.set(lobby, new HashSet<>());
        field.setAccessible(false);
    }

    @Test
    public void registerUserTest() {
        Set<LobbyUserSession> userSessions = new HashSet<>();
        LobbyUserSession lobbyUserSession = new LobbyUserSession(1L, "123");
        userSessions.add(lobbyUserSession);
        lobby.registerUser(lobbyUserSession);
        Assert.assertEquals(lobby.getUsers(), userSessions);
    }

}
