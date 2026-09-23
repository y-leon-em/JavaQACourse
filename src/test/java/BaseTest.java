import annotations.BrowserMode;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import annotations.BrowserMode.Mode;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("Запуск теста, инициализация драйвера");
        WebDriverManager.chromedriver().setup();

        Mode mode = testInfo.getTestMethod()
                .map(method -> method.getAnnotation(BrowserMode.class))
                .map(BrowserMode::value)
                .orElse(Mode.HEADLESS);

        ChromeOptions options = new ChromeOptions();
        switch (mode) {
            case HEADLESS -> options.addArguments("--headless=new");
            case KIOSK -> options.addArguments("--kiosk");
            case FULLSCREEN -> options.addArguments("--start-fullscreen");
        }
        driver = new ChromeDriver(options);
        log.debug("Chrome запущен, sessionId={}",
                ((RemoteWebDriver) driver).getSessionId());
    }

    @AfterEach
    void tearDown() {
        log.info("Завершение теста, закрытие драйвера");
        if (driver != null) {
            driver.quit();
        }
    }
}
