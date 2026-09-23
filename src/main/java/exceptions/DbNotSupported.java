package exceptions;

public class DbNotSupported extends RuntimeException {
    public DbNotSupported(String dbType) {
        super(String.format("The database type %s is not supported", dbType));
    }
}
