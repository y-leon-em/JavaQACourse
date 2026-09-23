package db;

import settings.DBSettingsReader;

import java.sql.*;
import java.util.Map;

public class SQLConnectionManager implements DBConnectionManager {

    private static Connection connection = null;
    private static Statement statement = null;

    DBSettingsReader dbSettingsReader = new DBSettingsReader();
    Map<String, String> settings = dbSettingsReader.getSettings();

    public void sqlConnectionManager() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(settings.get("URL"), settings.get("USER"),
                    settings.get("PASSWORD"));
        }


        if (statement == null) {
            statement = connection.createStatement();
        }
    }
    public ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public ResultSet executeWithData(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }

    public void execute(String sqlRequest) throws SQLException {
        statement.execute(sqlRequest);
    }

    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
