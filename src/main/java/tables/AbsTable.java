package tables;

import java.util.List;

public abstract class AbsTable<T> {

    protected final String tableName;

    public AbsTable(String tableName) {
        this.tableName = tableName;
    }

    public abstract void create() throws Exception;

    public abstract void save(T entity) throws Exception;

    public abstract List<T> list() throws Exception;

    public abstract void update(T entity) throws Exception;

    public abstract void delete(int id) throws Exception;

    public abstract T findById(int id) throws Exception;

    public abstract List<T> findByType(String type) throws Exception;
}