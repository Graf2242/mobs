package ResourceSystem.Resources.configs;

import Serialization.Serializator;
import utils.Resource;

public class ServerConfig implements Resource {
    NodeConfig master;

    FrontendConfig frontend;

    NodeConfig dbService;

    NodeConfig lobby;

    NodeConfig mechanics;

    String databasePath;
    String dbLogin;
    String dbPass;

    public ServerConfig() {
        master = new NodeConfig();
        frontend = new FrontendConfig();
        dbService = new NodeConfig();
        lobby = new NodeConfig();
        mechanics = new NodeConfig();
    }

    public static ServerConfig newInstance(String path) {
        try {
            return (ServerConfig) Serializator.deserializeXmlFile(path);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NodeConfig getMaster() {
        return master;
    }

    public void setMaster(NodeConfig master) {
        this.master = master;
    }

    public FrontendConfig getFrontend() {
        return frontend;
    }

    public void setFrontend(FrontendConfig frontend) {
        this.frontend = frontend;
    }

    public NodeConfig getDbService() {
        return dbService;
    }

    public void setDbService(NodeConfig dbService) {
        this.dbService = dbService;
    }

    public NodeConfig getLobby() {
        return lobby;
    }

    public void setLobby(NodeConfig lobby) {
        this.lobby = lobby;
    }

    public NodeConfig getMechanics() {
        return mechanics;
    }

    public void setMechanics(NodeConfig mechanics) {
        this.mechanics = mechanics;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    public String getDbLogin() {
        return dbLogin;
    }

    public void setDbLogin(String dbLogin) {
        this.dbLogin = dbLogin;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }
}
