package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    final WebDriver driver;
    final WebDriverWait wait;
    final JavascriptExecutor js;

    final String BASE_URL= "https://wishlist.otus.kartushin.su";

    BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void setValueViaJs(By locator, String value) {
        WebElement element = waitVisible(locator);
        js.executeScript(
                "const el = arguments[0];" +
                        "const val = arguments[1];" +
                        "const setter = Object.getOwnPropertyDescriptor(" +
                        "    window.HTMLInputElement.prototype, 'value').set;" +
                        "el.focus();" +
                        "setter.call(el, '');" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "setter.call(el, val);" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true }));" +
                        "el.blur();",
                element, value
        );
    }

    protected String getInputValue(By locator) {
        WebElement element = waitVisible(locator);
        return element.getAttribute("value");
    }

    public void clickOnNavItem(By locator) {
        WebElement element = waitVisible(locator);
        element.click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

}
