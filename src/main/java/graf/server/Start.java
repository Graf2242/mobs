package graf.server;

import main.*;

import java.util.Objects;

public class Start {

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "base/src/main/resources/configs/config.xml" : arg;

        String[] outArgs = new String[1];
        outArgs[0] = configPath;
        MasterServiceImpl.main(outArgs);
        HDBServiceImpl.main(outArgs);
        FrontendImpl.main(outArgs);
        LobbyImpl.main(outArgs);
        GameMechanicsImpl.main(outArgs);
/*
        MasterService masterService = new MasterServiceImpl(configPath);
        Thread masterThread = new Thread(masterService);
        masterThread.start();

        DBService dbService = new HDBServiceImpl(configPath);
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.start();

        Frontend frontend = new FrontendImpl(configPath);
        Thread frontendThread = new Thread(frontend);
        frontendThread.setName("frontend");
        frontendThread.start();

        Lobby lobby = new LobbyImpl(configPath);
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.setName("LOBBY");
        lobbyThread.start();

        GameMechanics gameMechanics = new GameMechanicsImpl(configPath);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();
        */
    }

}
