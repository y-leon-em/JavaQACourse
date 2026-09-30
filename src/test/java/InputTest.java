import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.LoginPage;
import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.junit.jupiter.api.Test;

public class InputTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(InputTest.class);

    @Test
    @BrowserMode(Mode.HEADLESS)
    void openResourceAndInputText() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .open()
                .typeUserName("ОТУС")
                .assertUserNameIs("ОТУС");

        log.info("Тест пройден успешно");
    }
}
