package graf.server;

import graf.server.AccountService.AccountServiceImpl;
import graf.server.Base.*;
import graf.server.Frontend.FrontendImpl;
import graf.server.GameMechanics.GameMechanicsImpl;
import graf.server.Lobby.LobbyImpl;
import graf.server.MasterService.MasterServiceImpl;

public class Main {

    public static void main(String[] args) {
        MasterService masterService = new MasterServiceImpl();
        Thread masterThread = new Thread(masterService);
        masterThread.start();

        AccountService AccountService = new AccountServiceImpl(masterService);
        Thread AccountServiceThread = new Thread(AccountService);
        AccountServiceThread.setName("AccountService");
        AccountServiceThread.start();

        Frontend frontend = new FrontendImpl(masterService);
        Thread frontendThread = new Thread(frontend);
        frontendThread.setName("Frontend");
        frontendThread.start();

        Lobby lobby = new LobbyImpl(masterService);
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.setName("Lobby");
        lobbyThread.start();

        GameMechanics gameMechanics = new GameMechanicsImpl(masterService);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();
    }


}