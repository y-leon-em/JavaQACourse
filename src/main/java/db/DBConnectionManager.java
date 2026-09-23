package db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBConnectionManager {
    public void execute(String sqlRequest) throws SQLException;

    public void close() throws SQLException;
    public ResultSet executeWithData(String sql) throws SQLException ;
}
