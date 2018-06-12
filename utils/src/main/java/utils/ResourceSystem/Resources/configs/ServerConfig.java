package utils.ResourceSystem.Resources.configs;

import base.utils.Resource;
import utils.Serialization.SerializerHelper;
import utils.logger.LoggerImpl;

public class ServerConfig implements Resource {
    private NodeConfig master;

    private DoubledConfig frontend;

    private DoubledConfig metrics;

    private NodeConfig dbService;

    private NodeConfig lobby;

    private NodeConfig mechanics;

    private String databasePath;

    private String dbLogin;

    private String dbPass;

    public ServerConfig() {
        master = new NodeConfig();
        frontend = new DoubledConfig();
        dbService = new NodeConfig();
        lobby = new NodeConfig();
        mechanics = new NodeConfig();
        metrics = new DoubledConfig();
    }

    public static ServerConfig newInstance(String path) {
        try {
            return (ServerConfig) SerializerHelper.deserializeXmlFile(path);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LoggerImpl.getLogger().error(e);
        }
        return null;
    }

    public DoubledConfig getMetrics() {
        return metrics;
    }

    public void setMetrics(DoubledConfig metrics) {
        this.metrics = metrics;
    }

    public NodeConfig getMaster() {
        return master;
    }

    public void setMaster(NodeConfig master) {
        this.master = master;
    }

    public DoubledConfig getFrontend() {
        return frontend;
    }

    public void setFrontend(DoubledConfig frontend) {
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
