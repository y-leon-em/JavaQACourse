package db;

import settings.DBSettingsReader;

import java.sql.*;
import java.util.Map;

public class SQLConnectionManager implements DBConnectionManager {

    private static Connection connection = null;
    private static Statement statement = null;

    DBSettingsReader dbSettingsReader = new DBSettingsReader();
    Map<String, String> settings = dbSettingsReader.getSettings();

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(settings.get("URL"), settings.get("USER"),
                    settings.get("PASSWORD"));
        }
        return connection;
    }

    public Statement getStatement() throws SQLException {
        Connection conn = getConnection();
        if (statement == null || statement.isClosed()) {
            statement = conn.createStatement();
        }
        return statement;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    public ResultSet executeWithData(String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        return getStatement().executeUpdate(sql);
    }

    public void execute(String sqlRequest) throws SQLException {
        getStatement().execute(sqlRequest);
    }

    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
