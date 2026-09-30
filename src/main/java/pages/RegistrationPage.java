package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationPage extends BasePage {
    private static final String PAGE_PATH = "/register";

    private static final By USER_NAME_INPUT = By.cssSelector("input[type='text']");
    private static final By USER_EMAIL_INPUT = By.cssSelector("input[type='email']");
    private static final By USER_PASSWORD_INPUT = By.cssSelector("input[type='password']");
    private static final By REGISTRATION_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ALERT_BADGE = By.cssSelector("*[class*=alert-danger]");

    public  RegistrationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return PAGE_PATH;
    }

    public RegistrationPage open() {
        openPage();
        return this;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    /**
     * Вводит имя пользователя через реальные события клавиатуры.
     *
     * @param text имя пользователя
     */
    public RegistrationPage typeUserName(String text) {
        type(USER_NAME_INPUT, text);
        return this;
    }

    /**
     * Вводит имя пользователя через JavaScript.
     *
     * @param text имя пользователя
     */
    public RegistrationPage typeUserNameViaJS(String text) {
        setValueViaJs(USER_NAME_INPUT, text);
        return this;
    }

    /**
     * Вводит email через реальные события клавиатуры.
     *
     * @param text email
     */
    public RegistrationPage typeUserEmail(String text) {
        type(USER_EMAIL_INPUT, text);
        return this;
    }

    /**
     * Вводит email через JavaScript.
     *
     * @param text email
     */
    public RegistrationPage typeUserEmailViaJS(String text) {
        setValueViaJs(USER_EMAIL_INPUT, text);
        return this;
    }

    /**
     * Вводит пароль через реальные события клавиатуры.
     *
     * @param text пароль
     */
    public RegistrationPage typeUserPassword(String text) {
        type(USER_PASSWORD_INPUT, text);
        return this;
    }

    /**
     * Вводит пароль через JavaScript.
     *
     * @param text пароль
     */
    public RegistrationPage typeUserPasswordViaJS(String text) {
        setValueViaJs(USER_PASSWORD_INPUT, text);
        return this;
    }

    /**
     * Возвращает текущее значение поля пароля.
     *
     * @return введённый пароль
     */
    public String getUserPasswordValue() {
        return getInputValue(USER_PASSWORD_INPUT);
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке регистрации.
     *
     * @return {@code true}, если сообщение отображается
     */
    public boolean alertRegistrationBadgeIsDisplayed() {
        WebElement element = waitVisible(ALERT_BADGE);
        return element.isDisplayed();
    }

    /**
     * Возвращает сообщение HTML-валидации поля email.
     *
     * @return текст сообщения или пустая строка
     */
    public String getEmailValidationMessage() {
        return getInputValidationMessage(USER_EMAIL_INPUT);
    }

    /**
     * Нажимает кнопку регистрации.
     *
     */
    public RegistrationPage clickRegistrationButton() {
        WebElement element = waitVisible(REGISTRATION_BUTTON);
        element.click();
        return this;
    }

    /**
     * Проверяет, что сообщение валидации email совпадает с ожидаемым.
     *
     * @param expected ожидаемое сообщение
     */
    public RegistrationPage assertEmailValidationMessageIs(String expected) {
        assertEquals(expected, getEmailValidationMessage(),
                "Сообщение валидации email не совпадает");
        return this;
    }

    /**
     * Проверяет, что отображается сообщение об ошибке при повторной регистрации.
     *
     */
    public RegistrationPage assertAlertRegistrationIsDisplayed() {
        assertTrue(alertRegistrationBadgeIsDisplayed(),
                "Ожидалось сообщение об ошибке при повторной регистрации");
        return this;
    }

    /**
     * Проверяет, что введённый пароль совпадает с ожидаемым.
     *
     * @param expected ожидаемый пароль
     */
    public RegistrationPage assertPasswordIs(String expected) {
        assertEquals(expected, getUserPasswordValue(), "Введенный пароль не совпадает с ожидаемым");
        return this;
    }


}
