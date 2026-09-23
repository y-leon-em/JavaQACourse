package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WishListPage extends BasePage {
    final String PATH = "/wishlists";

    private final By wishListHeader = By.xpath("//*[text()='Мои списки желаний']");

    public WishListPage(WebDriver driver) {super(driver);}

    public String getUrl() {
        return getCurrentUrl();
    }

    public Boolean wishListHeaderIsDisplayed() {
        WebElement element = waitVisible(wishListHeader);
        return element.isDisplayed();
    }
}
