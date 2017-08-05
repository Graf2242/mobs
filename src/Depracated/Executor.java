package graf.server.Utils.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {
    public static <T> T exec(Connection connection, String query, ResultHandler<T> handler) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
        ResultSet result = statement.getResultSet();
        T value = handler.handle(result);
        statement.close();
        return value;
    }
}
