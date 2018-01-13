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
    }

}
