package graf.server.MasterService;

import graf.server.Utils.DB.ResultHandler;
import graf.server.Utils.ResourceSystem.ResourceFactory;
import graf.server.Utils.ResourceSystem.Resources.ServerConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseCreator {
    Connection connection;
    ResourceFactory resourceFactory = ResourceFactory.instance();
    Map<Integer, String[]> converts = new HashMap<>();
    Integer version;

    public DatabaseCreator() {
        connectToDB();
        getCurrentDBVersion();
        converts = resourceFactory.readAllTextFiles("src/main/resources/converts");
        execConverts();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void execConverts() {
        final Integer[] convertVersion = new Integer[1];
        converts.forEach((integer, strings) -> {
            StringBuilder builder = new StringBuilder();
            convertVersion[0] = integer;
            if (version < convertVersion[0]) {
                for (String string : strings) {
                    builder.append(string);
                }
            }
            String string = builder.toString();
            if (!Objects.equals(string, "")) {


                try {
                    execQuery(string, resultSet -> null);
                    updateVersion(convertVersion);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateVersion(Integer[] convertVersion) throws SQLException {
        if (version < 0) {
            execQuery("INSERT into public.dbinfo (version) values (0)", resultSet -> null);
            version = 0;
            return;
        }
        execQuery(String.format("update dbinfo set version = %s", convertVersion[0]), resultSet -> null);
        version = convertVersion[0];
    }

    private void getCurrentDBVersion() {
        try {
            version = execQuery("select version from public.dbinfo", (ResultHandler<Integer>) resultSet -> {
                resultSet.next();
                return resultSet.getInt(1);
            });
        } catch (SQLException e) {
            version = -1;
        }
    }

    private <T> T execQuery(String query,
                            ResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        if (result != null) {
            result.close();
        }
        stmt.close();
        return value;
    }

    private void connectToDB() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("org.postgresql.Driver").newInstance());
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        ServerConfig serverConfig = (ServerConfig) resourceFactory.getResource("src/main/resources/resources/config");
        try {
            connection = DriverManager.getConnection(serverConfig.getDatabasePath(), serverConfig.getDbLogin(), serverConfig.getDbPass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
