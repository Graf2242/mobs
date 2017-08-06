package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultHandler {
    public interface TResultHandler<T> {
        T handle(ResultSet resultSet) throws SQLException;
    }
}
