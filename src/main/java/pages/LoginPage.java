package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    private final String PATH = "/login";

    private final By userNameInput = By.cssSelector("input[type='text']");
    private final By userPasswordInput = By.cssSelector("input[type='password']");
    private final By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(BASE_URL + PATH);
        return this;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public LoginPage typeUserName(String text) {
        type(userNameInput, text);
        return this;
    }

    public String getUserNameValue() {
        return getInputValue(userNameInput);
    }

    public LoginPage typeUserPassword(String text) {
        type(userPasswordInput, text);
        return this;
    }

    public LoginPage clickLoginButton() {
        WebElement element = waitVisible(loginButton);
        element.click();
        return this;
    }

}
