package graf.server;

import graf.server.Base.*;
import graf.server.DBService.DBServiceImpl;
import graf.server.Frontend.FrontendImpl;
import graf.server.GameMechanics.GameMechanicsImpl;
import graf.server.Lobby.LobbyImpl;
import graf.server.MasterService.MasterServiceImpl;
import graf.server.Utils.ResourceSystem.ResourceFactory;

public class Main {

    public static void main(String[] args) {
        ResourceFactory.instance();

        MasterService masterService = new MasterServiceImpl();
        Thread masterThread = new Thread(masterService);
        masterThread.start();

        DBService dbService = new DBServiceImpl(masterService);
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.start();

        Frontend frontend = new FrontendImpl(masterService);
        Thread frontendThread = new Thread(frontend);
        frontendThread.setName("Frontend");
        frontendThread.start();

        Lobby lobby = new LobbyImpl(masterService);
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.setName("LOBBY");
        lobbyThread.start();

        GameMechanics gameMechanics = new GameMechanicsImpl(masterService);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();
    }


}