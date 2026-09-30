import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import pages.WishListPage;
import utils.TestDataGenerator;

public class LoginTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(LoginTest.class);

    @Test
    @DisplayName("Регистрация и вход на сайт")
    @BrowserMode(Mode.KIOSK)
    void registrationAndLogin() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);
        WishListPage wishListPage = new WishListPage(driver);

        registrationPage
                .open()
                .typeUserName(userName)
                .typeUserEmail(userEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .waitForUrlContains(loginPage.getPath());

        loginPage
                .assertRedirectOnPage()
                .typeUserName(userName)
                .typeUserPassword(userPassword)
                .clickLoginButton()
                .waitForUrlContains(wishListPage.getPath());

        wishListPage
                .assertRedirectOnPage()
                .assertHeaderIsDisplayed();

        log.info("Тест пройден успешно");
    }

    @Test
    @DisplayName("Попытка входа незарегистрированного пользователя")
    @BrowserMode(Mode.MAXIMIZED)
    void unregisteredLogin() {
        final String userName = TestDataGenerator.randomName();
        final String userPassword = TestDataGenerator.randomPassword();

        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .open()
                .typeUserName(userName)
                .typeUserPassword(userPassword)
                .clickLoginButton()
                .assertAlertInvalidUserIsDisplayed();

        log.info("Тест пройден успешно");
    }
}
