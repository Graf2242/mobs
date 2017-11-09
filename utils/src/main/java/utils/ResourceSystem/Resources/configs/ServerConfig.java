package utils.ResourceSystem.Resources.configs;

import base.utils.Resource;
import utils.Serialization.Serializator;
import utils.logger.LoggerImpl;

public class ServerConfig implements Resource {
    private NodeConfig master;

    private FrontendConfig frontend;

    private NodeConfig dbService;

    private NodeConfig lobby;

    private NodeConfig mechanics;

    private String databasePath;
    private String dbLogin;
    private String dbPass;

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
            LoggerImpl.getLogger().error(e);
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
