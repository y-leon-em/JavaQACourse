import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import utils.TestDataGenerator;

public class ExecuteJavaScriptTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(ExecuteJavaScriptTest.class);

    @Test
    @BrowserMode(Mode.FULLSCREEN)
    void inputFormViaJS() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage
                .open()
                .typeUserNameViaJS(userName)
                .typeUserEmailViaJS(userEmail)
                .typeUserPasswordViaJS(userPassword)
                .assertPasswordIs(userPassword)
                .clickRegistrationButton()
                .waitForUrlContains(loginPage.getPath());
        loginPage.assertRedirectOnPage();

        log.info("Тест пройден успешно");
    }
}
