package config;

public final class Config {

    private static final String DEFAULT_BASE_URL = "https://wishlist.otus.kartushin.su";
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final String DEFAULT_BROWSER = "chrome";

    private Config() {}

    /**
     * Возвращает значение system property или дефолт, если параметр не задан.
     */
    private static String resolve(String key, String defaultValue) {
        String value = System.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    /**
     * Базовый URL тестируемого стенда.
     * Переопределяется через {@code -Dbase.url=...}.
     */
    public static String baseUrl() {
        return resolve("base.url", DEFAULT_BASE_URL);
    }

    /**
     * Таймаут ожиданий в секундах.
     * Переопределяется через {@code -Dbrowser.timeout=...}.
     */
    public static int timeout() {
        return Integer.parseInt(
                resolve("browser.timeout", String.valueOf(DEFAULT_TIMEOUT_SECONDS)));
    }

    /**
     * Имя браузера по умолчанию.
     * Переопределяется через {@code -Dbrowser=...}.
     */
    public static String browser() {
        return resolve("browser", DEFAULT_BROWSER);
    }
}