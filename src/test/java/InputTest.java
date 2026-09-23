import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.LoginPage;
import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(InputTest.class);

    @Test
    @BrowserMode(Mode.HEADLESS)
    void openResourceAndInputText() {
        LoginPage page = new LoginPage(driver)
                .open()
                .typeUserName("ОТУС");
        assertEquals("ОТУС", page.getUserNameValue(), "Введённый текст не совпадает");
        log.info("Тест пройден успешно");
    }
}
