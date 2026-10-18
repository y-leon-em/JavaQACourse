package tables;

import animals.Animal;
import db.SQLConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class AnimalTable extends AbsTable<Animal> {

    private final SQLConnectionManager cm;
    private final AnimalDBBinding binding;

    public AnimalTable(SQLConnectionManager cm) {
        super("animals");
        this.cm = cm;
        this.binding = new AnimalDBBinding(cm);
    }

    @Override
    public void create() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS animals (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255)," +
                "age INT," +
                "weight DOUBLE," +
                "color VARCHAR(50)," +
                "type VARCHAR(50)" +
                ")";
        cm.executeUpdate(sql);
    }

    @Override
    public void save(Animal entity) throws Exception {
        binding.save(entity);
    }

    @Override
    public List<Animal> list() throws Exception {
        return binding.findAll();
    }

    @Override
    public void update(Animal entity) throws Exception {
        binding.update(entity);
    }

    @Override
    public void delete(int id) throws Exception {
        binding.delete(id);
    }

    @Override
    public Animal findById(int id) throws Exception {
        return binding.findById(id);
    }

    @Override
    public List<Animal> findByType(String type) throws Exception {
        return binding.findByType(type);
    }
}