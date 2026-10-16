package factory;

import annotations.BrowserMode.Mode;
import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public final class BrowserModeFactory {

    private static final Logger log = LogManager.getLogger(BrowserModeFactory.class);

    private BrowserModeFactory() {}

    /**
     * Создаёт WebDriver с браузером из конфига и указанным режимом окна.
     *
     * @param mode режим окна (HEADLESS, KIOSK, FULLSCREEN, MAXIMIZED)
     * @return экземпляр WebDriver
     */
    public static WebDriver create(Mode mode) {
        String browser = Config.browser().toLowerCase(Locale.ROOT).trim();
        log.info("Инициализация браузера: {}, режим: {}", browser, mode);

        Capabilities options = buildOptions(browser, mode);
        WebDriver driver = WebDriverFactory.create(browser, options);

        if (mode == Mode.MAXIMIZED) {
            driver.manage().window().maximize();
        }

        return driver;
    }

    /**
     * Собирает опции браузера с учётом режима.
     */
    private static Capabilities buildOptions(String browser, Mode mode) {
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions opts = new FirefoxOptions();
                applyFirefoxMode(opts, mode);
                yield opts;
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                applyChromiumMode(opts, mode);
                yield opts;
            }
            case "chrome" -> {
                ChromeOptions opts = new ChromeOptions();
                applyChromiumMode(opts, mode);
                yield opts;
            }
            default -> null;
        };
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