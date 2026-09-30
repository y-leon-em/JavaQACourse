import annotations.BrowserMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import annotations.BrowserMode.Mode;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("Запуск теста, инициализация драйвера");

        Mode mode = testInfo.getTestMethod()
                .map(method -> method.getAnnotation(BrowserMode.class))
                .map(BrowserMode::value)
                .orElse(Mode.HEADLESS);

        driver = BrowserFactory.create(mode);

        log.debug("Драйвер запущен, sessionId={}",
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
