package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegistrationPage extends BasePage {
    final String PATH = "/register";

    private final By userNameInput = By.cssSelector("input[type='text']");
    private final By userEmailInput = By.cssSelector("input[type='email']");
    private final By userPasswordInput = By.cssSelector("input[type='password']");
    private final By registrationButton = By.cssSelector("button[type='submit']");

    public  RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public RegistrationPage open() {
        driver.get(BASE_URL + PATH);
        return this;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public RegistrationPage typeUserName(String text) {
        type(userNameInput, text);
        return this;
    }

    public RegistrationPage typeUserEmail(String text) {
        type(userEmailInput, text);
        return this;
    }

    public RegistrationPage typeUserPassword(String text) {
        type(userPasswordInput, text);
        return this;
    }

    public RegistrationPage clickRegistrationButton() {
        WebElement element = waitVisible(registrationButton);
        element.click();
        return this;
    }
}
