package pages;

import config.Config;
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

    BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.timeout()));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Ждёт появления элемента на странице и возвращает его.
     *
     * @param locator локатор элемента
     * @return найденный видимый элемент
     */
    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Ждёт, пока текущий URL будет содержать указанную подстроку.
     *
     * @param part подстрока, которая должна быть в URL
     */
    public void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }

    /**
     * Возвращает относительный путь страницы, использующийся при открытии.
     *
     * @return путь, например {@code /login}
     */
    protected abstract String getPath();

    public void openPage() {
        driver.get(Config.baseUrl() + getPath());
    }

    /**
     * Очищает поле и вводит в него текст.
     *
     * @param locator локатор поля ввода
     * @param text    текст для ввода
     */
    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Устанавливает значение поля через JavaScript.
     *
     * @param locator локатор поля ввода
     * @param value   значение для установки
     */
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

    /**
     * Возвращает сообщение HTML-валидации поля ввода.
     * Пустая строка означает, что поле валидно.
     *
     * @param locator локатор поля ввода
     * @return текст сообщения валидации или пустая строка
     */
    protected String getInputValidationMessage(By locator) {
        WebElement element = waitVisible(locator);
        Object result = js.executeScript(
                "return arguments[0].validationMessage;",
                element
        );
        return result == null ? "" : result.toString();
    }

    /**
     * Возвращает текущее значение поля ввода (атрибут value).
     *
     * @param locator локатор поля ввода
     * @return значение поля
     */
    protected String getInputValue(By locator) {
        WebElement element = waitVisible(locator);
        return element.getAttribute("value");
    }

    /**
     * Кликает по элементу навигации, дожидаясь его видимости.
     *
     * @param locator локатор элемента навигации
     */
    public void clickOnNavItem(By locator) {
        WebElement element = waitVisible(locator);
        element.click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

}
