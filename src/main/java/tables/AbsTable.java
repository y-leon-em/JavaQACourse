package tables;

import animals.Animal;
import db.DBConnectionManager;
import factory.DBFactory;

import java.util.List;

public class AbsTable {

    private String tableName;

    private DBConnectionManager dbConnectionManager;

    public AbsTable(String tableName) {
        this.dbConnectionManager = new DBFactory().getDBConnectionManager("mysql");
        this.tableName = tableName;
    }
    public List<T> list(){
        dbConnectionManager.executeWithData(String.format("select * from table %s", tableName));

        return List.of();
    }
}
