import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import utils.TestDataGenerator;

import static components.NavigationBar.registrationLink;

public class RegistrationTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(RegistrationTest.class);

    @Test
    @DisplayName("Регистрация пользователя")
    @BrowserMode(Mode.FULLSCREEN)
    void registration() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage
                .open()
                .typeUserName(userName)
                .typeUserEmail(userEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .waitForUrlContains(loginPage.getPath());
        loginPage.assertRedirectOnPage();

        log.info("Тест пройден успешно");
    }

    @Test
    @DisplayName("Регистрация с невалидным email'ом и проверка валидации поля ввода")
    @BrowserMode(Mode.MAXIMIZED)
    void invalidRegistration() {
        final String userName = TestDataGenerator.randomName();
        final String invalidEmail = "hsgljsg.com";
        final String userPassword = TestDataGenerator.randomPassword();
        final String emailValidationMessage = String
                .format("Адрес электронной почты должен содержать символ \"@\". В адресе \"%s\" отсутствует символ \"@\".", invalidEmail);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage
                .open()
                .typeUserName(userName)
                .typeUserEmail(invalidEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .assertEmailValidationMessageIs(emailValidationMessage);

        log.info("Тест пройден успешно");
    }

    @Test
    @DisplayName("Регистрация уже зарегистрированного пользователя")
    @BrowserMode(Mode.MAXIMIZED)
    void repeatRegistration() {
        final String userName = TestDataGenerator.randomName();
        final String userEmail = TestDataGenerator.randomEmail();
        final String userPassword = TestDataGenerator.randomPassword();
        final String loginPath = "/login";

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage
                .open()
                .typeUserName(userName)
                .typeUserEmail(userEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .waitForUrlContains(loginPath);
        registrationPage.clickOnNavItem(registrationLink);
        registrationPage
                .typeUserName(userName)
                .typeUserEmail(userEmail)
                .typeUserPassword(userPassword)
                .clickRegistrationButton()
                .assertAlertRegistrationIsDisplayed();

        log.info("Тест пройден успешно");
    }
}
