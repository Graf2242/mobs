package utils.ResourceSystem;

import org.apache.logging.log4j.Logger;
import utils.ResourceSystem.Resources.configs.FrontendConfig;
import utils.ResourceSystem.Resources.configs.NodeConfig;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.Serialization.Serializator;
import utils.logger.LoggerImpl;


public class ResourceCreator {

    private static final String path = "src/test/resources/testConfig";

    public static void main(String[] args) {
        createServerConfig();
        try {
            LoggerImpl.getLogger().info(Serializator.deserializeXmlFile(path));
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
        config.setPort("9091");
        serverConfig.setDbService(config);
        FrontendConfig fConfig = new FrontendConfig();
        fConfig.setIp("localhost");
        fConfig.setPort("9092");
        fConfig.setFrontendPort("9093");
        serverConfig.setFrontend(fConfig);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9094");
        serverConfig.setLobby(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9095");
        serverConfig.setMaster(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9096");
        serverConfig.setMechanics(config);

        Serializator.serializeXmlFile(serverConfig, path);
    }
}
