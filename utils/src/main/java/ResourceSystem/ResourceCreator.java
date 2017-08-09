package ResourceSystem;

import ResourceSystem.Resources.configs.NodeConfig;
import ResourceSystem.Resources.configs.ServerConfig;
import Serialization.Serializator;


public class ResourceCreator {

    private static final String path = "base/src/main/resources/configs/config.xml";

    public static void main(String[] args) {
//        createServerConfig();
        try {
            System.out.println(Serializator.deserializeXmlFile(path));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void createServerConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setDatabasePath("jdbc:postgresql://localhost:5432/mobs");
        serverConfig.setDbLogin("postgres");
        serverConfig.setDbPass("mobs");

        NodeConfig config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9091");
        serverConfig.setDbService(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9092");
        serverConfig.setFrontend(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9093");
        serverConfig.setLobby(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9094");
        serverConfig.setMaster(config);
        config = new NodeConfig();
        config.setIp("localhost");
        config.setPort("9095");
        serverConfig.setMechanics(config);

        Serializator.serializeXmlFile(serverConfig, path);
    }
}
