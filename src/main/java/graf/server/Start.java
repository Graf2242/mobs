package graf.server;


import ResourceSystem.ResourceFactory;
import databaseService.DBService;
import frontend.Frontend;
import gameMechanics.GameMechanics;
import lobby.Lobby;
import main.*;
import masterService.MasterService;

import java.util.Objects;

public class Start {

    public static void main(String[] args) {
        ResourceFactory.instance();
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "base/src/main/resources/configs/config.xml" : arg;

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
