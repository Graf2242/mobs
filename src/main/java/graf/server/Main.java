package graf.server;

import ResourceSystem.ResourceFactory;
import frontend.Frontend;
import gameMechanics.GameMechanics;
import graf.server.Base.DBService;
import graf.server.Base.Lobby;
import graf.server.Base.MasterService;
import graf.server.Frontend.FrontendImpl;
import graf.server.GameMechanics.GameMechanicsImpl;
import graf.server.Lobby.LobbyImpl;
import graf.server.MasterService.MasterServiceImpl;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        ResourceFactory.instance();
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "src/main/resources/resources/config" : arg;

        MasterService masterService = new MasterServiceImpl(configPath);
        Thread masterThread = new Thread(masterService);
        masterThread.start();

        DBService dbService = new HDBServiceImpl(masterService, configPath);
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.start();

        Frontend frontend = new FrontendImpl(masterService, configPath);
        Thread frontendThread = new Thread(frontend);
        frontendThread.setName("frontend");
        frontendThread.start();

        Lobby lobby = new LobbyImpl(masterService, configPath);
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.setName("LOBBY");
        lobbyThread.start();

        GameMechanics gameMechanics = new GameMechanicsImpl(masterService, configPath);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();
    }


}