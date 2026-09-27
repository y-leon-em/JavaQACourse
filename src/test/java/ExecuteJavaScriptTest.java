import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import utils.TestDataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecuteJavaScriptTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(InputTest.class);

    @Test
    @BrowserMode(Mode.FULLSCREEN)
    void inputFormViaJS() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();
        final String loginPath = "/login";

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage
                .open()
                .typeUserNameViaJS(userName)
                .typeUserEmailViaJS(userEmail)
                .typeUserPasswordViaJS(userPassword);
        assertEquals(userPassword, registrationPage.getUserPasswordValue(),"Введенный пароль не совпадает с ожидаемым");

        registrationPage
                .clickRegistrationButton()
                .waitForUrlContains(loginPath);
        assertTrue(loginPage.getUrl().contains(loginPath), "После регистрации не произошел редирект");
        log.info("Тест пройден успешно");
    }
}
