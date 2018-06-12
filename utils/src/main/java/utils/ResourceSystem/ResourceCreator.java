package utils.ResourceSystem;

import org.apache.logging.log4j.Logger;
import utils.ResourceSystem.Resources.configs.DoubledConfig;
import utils.ResourceSystem.Resources.configs.NodeConfig;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.Serialization.SerializerHelper;
import utils.logger.LoggerImpl;


public class ResourceCreator {

    private static final String path = "src/test/resources/testConfig";

    public static void main(String[] args) {
        createServerConfig();
        try {
            LoggerImpl.getLogger().info(SerializerHelper.deserializeXmlFile(path));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Logger logger = LoggerImpl.getLogger();
            logger.traceEntry();
            logger.error(e);
            logger.traceExit();
        }

    }

    private static void createServerConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setDatabasePath("jdbc:postgresql://localhost:5432/testdb");
        serverConfig.setDbLogin("postgres");
        serverConfig.setDbPass("mobs");

        NodeConfig config = new NodeConfig();
        config.setIp("localhost");
        config.setMasterPort("9091");
        serverConfig.setDbService(config);
        DoubledConfig fConfig = new DoubledConfig();
        fConfig.setIp("localhost");
        fConfig.setMasterPort("9092");
        fConfig.setSecondPort("9093");
        serverConfig.setFrontend(fConfig);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setMasterPort("9094");
        serverConfig.setLobby(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setMasterPort("9095");
        serverConfig.setMaster(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setMasterPort("9096");
        serverConfig.setMechanics(config);

        SerializerHelper.serializeXmlFile(serverConfig, path);
    }
}
