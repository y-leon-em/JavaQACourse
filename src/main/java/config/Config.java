package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    private Config() {}

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static int timeout() {
        return Integer.parseInt(get("browser.timeout"));
    }
}
