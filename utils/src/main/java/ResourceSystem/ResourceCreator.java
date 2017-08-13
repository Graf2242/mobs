package ResourceSystem;

import ResourceSystem.Resources.configs.FrontendConfig;
import ResourceSystem.Resources.configs.NodeConfig;
import ResourceSystem.Resources.configs.ServerConfig;
import Serialization.Serializator;


public class ResourceCreator {

    private static final String path = "src/test/resources/testConfig";

    public static void main(String[] args) {
        createServerConfig();
        try {
            System.out.println(Serializator.deserializeXmlFile(path));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
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
