import annotations.BrowserMode.Mode;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public final class BrowserFactory {

    private static final Logger log = LogManager.getLogger(BrowserFactory.class);

    private static final String DEFAULT_BROWSER = "chrome";

    private BrowserFactory() {}

    public static WebDriver create(Mode mode) {
        String browser = System.getProperty("browser", DEFAULT_BROWSER)
                .toLowerCase(Locale.ROOT)
                .trim();

        log.info("Инициализация браузера: {}, режим: {}", browser, mode);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefox(mode);
            case "edge"    -> createEdge(mode);
            case "chrome"  -> createChrome(mode);
            default -> throw new IllegalArgumentException(
                    "Неподдерживаемый браузер: '" + browser + "'. " +
                            "Допустимые значения: chrome, firefox, edge");
        };

        if (mode == Mode.MAXIMIZED) {
            driver.manage().window().maximize();
        }

        return driver;
    }

    private static WebDriver createChrome(Mode mode) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        applyChromiumMode(options, mode);
        return new ChromeDriver(options);
    }

    private static WebDriver createEdge(Mode mode) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        applyChromiumMode(options, mode);
        return new EdgeDriver(options);
    }

    private static WebDriver createFirefox(Mode mode) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        applyFirefoxMode(options, mode);
        return new FirefoxDriver(options);
    }

    private static void applyChromiumMode(ChromiumOptions<?> options, Mode mode) {
        switch (mode) {
            case HEADLESS -> {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            case KIOSK -> options.addArguments("--kiosk");
            case FULLSCREEN -> options.addArguments("--start-fullscreen");
            case MAXIMIZED -> { /* применяется через driver.manage().window().maximize() */ }
        }
    }

    private static void applyFirefoxMode(FirefoxOptions options, Mode mode) {
        switch (mode) {
            case HEADLESS -> {
                options.addArguments("-headless");
                options.addArguments("--width=1920", "--height=1080");
            }
            case KIOSK -> options.addArguments("--kiosk");
            case FULLSCREEN -> options.addArguments("--start-fullscreen");
            case MAXIMIZED -> { /* применяется через driver.manage().window().maximize() */ }
        }
    }
}