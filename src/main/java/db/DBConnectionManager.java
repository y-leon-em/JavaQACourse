package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBConnectionManager {
    void execute(String sqlRequest) throws SQLException;

    void close() throws SQLException;

    ResultSet executeWithData(String sql) throws SQLException;

    Connection getConnection() throws SQLException;

    Statement getStatement() throws SQLException;
}
