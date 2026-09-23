package factory;

import db.DBConnectionManager;
import db.SQLConnectionManager;
import exceptions.DbNotSupported;

public class DBFactory {

    public DBConnectionManager getDBConnectionManager(String dbType) {
        switch (dbType) {
            case "mysql": return new SQLConnectionManager();
        }

        throw new DbNotSupported("The database type " + dbType + " is not supported");
    }
}
