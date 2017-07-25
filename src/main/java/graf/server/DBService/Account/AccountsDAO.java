package graf.server.DBService.Account;

import graf.server.Utils.DB.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountsDAO {
    final Connection connection;

    public AccountsDAO(Connection connection) {
        this.connection = connection;
    }

    public AccountsDataSet get(Long id) throws SQLException {
        return Executor.exec(connection, "select * from accounts where id = " + id, resultSet -> {
            resultSet.next();
            return new AccountsDataSet(resultSet.getLong("id"), resultSet.getString("login"), resultSet.getString("pass"));
        });
    }

    public AccountsDataSet get(String login) {
        try {
            return Executor.exec(connection, String.format("select * from accounts where (login = '%s')", login), resultSet -> {
                resultSet.next();
                return new AccountsDataSet(resultSet.getLong("id"), resultSet.getString("login"), resultSet.getString("pass"));
            });
        } catch (SQLException e) {
            return null;
        }
    }

    public AccountsDataSet writeAccount(String name, String pass) throws SQLException {
        Executor.exec(connection, String.format("insert into accounts (login, pass) values('%s', '%s')", name, pass), resultSet -> 0);
        return get(name);
    }
}
