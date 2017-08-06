package ResourceSystem.Resources;

import utils.Resource;

public class ServerConfig implements Resource {
    String ip;
    String port;
    String databasePath;
    String dbLogin;
    String dbPass;

    public ServerConfig() {
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public String getDbLogin() {
        return dbLogin;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
