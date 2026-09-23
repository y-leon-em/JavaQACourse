import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import pages.WishListPage;
import utils.TestDataGenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(InputTest.class);

    @Test
    @BrowserMode(Mode.KIOSK)
    void registrationAndLogin() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();
        final String loginPath = "/login";
        final String wishListPath = "/wishlists";

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);
        WishListPage wishListPage = new WishListPage(driver);

        registrationPage
                .open()
                .typeUserName(userName)
                .typeUserEmail(userEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .waitForUrlContains(loginPath);
        assertTrue(loginPage.getUrl().contains(loginPath), "После регистрации не произошел редирект");

        loginPage
                .typeUserName(userName)
                .typeUserPassword(userPassword)
                .clickLoginButton()
                .waitForUrlContains(wishListPath);
        assertTrue(wishListPage.getUrl().contains(wishListPath), "После входа не произошел редирект / неверные данные");
        assertTrue(wishListPage.wishListHeaderIsDisplayed(), "Не отображается заголовок страницы");
        log.info("Тест пройден успешно");
    }
}
