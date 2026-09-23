package tables;

import animals.Animal;
import db.SQLConnectionManager;

import java.sql.SQLException;

public class AnimalTable extends AbsTable<Animal> {
    private final SQLConnectionManager cm;

    public AnimalTable(String tableName,SQLConnectionManager cm) {

        super("animals");
        this.cm = cm;
    }

    public void create() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS animals_otus_petrukhno (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255)," +
                "age INT," +
                "weight INT," +
                "color VARCHAR(50)," +
                "type VARCHAR(50)" +
                ")";
        cm.executeUpdate(sql);
    }
}