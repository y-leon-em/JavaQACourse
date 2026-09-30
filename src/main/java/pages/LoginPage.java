package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage extends BasePage {
    private static final String PAGE_PATH = "/login";

    private static final By USER_NAME_INPUT = By.cssSelector("input[type='text']");
    private static final By USER_PASSWORD_INPUT = By.cssSelector("input[type='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ALERT_BADGE = By.cssSelector("*[class*=alert-danger]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPath() {
        return PAGE_PATH;
    }

    public LoginPage open() {
        openPage();
        return this;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    /**
     * Вводит имя пользователя в поле логина.
     *
     * @param text имя пользователя
     */
    public LoginPage typeUserName(String text) {
        type(USER_NAME_INPUT, text);
        return this;
    }

    /**
     * Возвращает текущее значение поля имени пользователя.
     *
     * @return введённое имя пользователя
     */
    public String getUserNameValue() {
        return getInputValue(USER_NAME_INPUT);
    }

    /**
     * Вводит пароль в поле логина.
     *
     * @param text пароль
     */
    public LoginPage typeUserPassword(String text) {
        type(USER_PASSWORD_INPUT, text);
        return this;
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке для невалидного пользователя.
     *
     * @return {@code true}, если сообщение отображается
     */
    public boolean alertInvalidUserBadgeIsDisplayed() {
        WebElement element = waitVisible(ALERT_BADGE);
        return element.isDisplayed();
    }

    /**
     * Нажимает кнопку входа.
     *
     */
    public LoginPage clickLoginButton() {
        WebElement element = waitVisible(LOGIN_BUTTON);
        element.click();
        return this;
    }

    /**
     * Проверяет, что введённое имя пользователя совпадает с ожидаемым.
     *
     * @param expected ожидаемое значение
     */
    public LoginPage assertUserNameIs(String expected) {
        assertEquals(expected, getUserNameValue(),
                "Введённое имя пользователя не совпадает");
        return this;
    }

    /**
     * Проверяет, что отображается сообщение об ошибке для незарегистрированного пользователя.
     *
     */
    public LoginPage assertAlertInvalidUserIsDisplayed() {
        assertTrue(alertInvalidUserBadgeIsDisplayed(),
                "Ожидалось сообщение об ошибке для незарегистрированного пользователя");
        return this;
    }

    /**
     * Проверяет, что произошёл редирект на страницу логина.
     *
     */
    public LoginPage assertRedirectOnPage() {
        assertTrue(getUrl().contains(getPath()), "Ожидался редирект на " + getPath() + ", но URL: " + getUrl());
        return this;
    }
}
