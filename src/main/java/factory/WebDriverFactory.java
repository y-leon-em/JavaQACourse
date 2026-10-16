package factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public final class WebDriverFactory {

    private WebDriverFactory() {}

    /**
     * Создаёт WebDriver по имени браузера с дефолтными опциями.
     * Пример: {@code WebDriverFactory.create("chrome")}
     *
     * @param webDriverName имя браузера: {@code chrome}, {@code firefox}, {@code edge}
     * @return экземпляр WebDriver
     * @throws IllegalArgumentException если имя браузера не поддерживается
     */
    public static WebDriver create(String webDriverName) {
        return create(webDriverName, null);
    }

    /**
     * Создаёт WebDriver по имени браузера с переданными опциями.
     * Если {@code options} не соответствует типу браузера — используются дефолтные опции.
     * Пример: {@code WebDriverFactory.create("firefox", new FirefoxOptions())}
     *
     * @param webDriverName имя браузера: {@code chrome}, {@code firefox}, {@code edge}
     * @param options       опции браузера или {@code null} для дефолтных
     * @return экземпляр WebDriver
     * @throws IllegalArgumentException если имя браузера не поддерживается
     */
    public static WebDriver create(String webDriverName, Capabilities options) {
        String name = webDriverName.toLowerCase(Locale.ROOT).trim();

        return switch (name) {
            case "chrome"  -> createChrome(options);
            case "firefox" -> createFirefox(options);
            case "edge"    -> createEdge(options);
            default -> throw new IllegalArgumentException(
                    "Неподдерживаемый браузер: '" + webDriverName + "'. " +
                            "Допустимые значения: chrome, firefox, edge");
        };
    }

    private static WebDriver createChrome(Capabilities options) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = (options instanceof ChromeOptions co) ? co : new ChromeOptions();
        return new ChromeDriver(opts);
    }

    private static WebDriver createFirefox(Capabilities options) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opts = (options instanceof FirefoxOptions fo) ? fo : new FirefoxOptions();
        return new FirefoxDriver(opts);
    }

    private static WebDriver createEdge(Capabilities options) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opts = (options instanceof EdgeOptions eo) ? eo : new EdgeOptions();
        return new EdgeDriver(opts);
    }
}