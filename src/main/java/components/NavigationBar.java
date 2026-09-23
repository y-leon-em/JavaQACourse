package components;

import org.openqa.selenium.By;

public class NavigationBar {
    public static final By loginLink = By.cssSelector("nav a[href='/login']");
    public static final By registrationLink = By.cssSelector("nav a[href='/register']");
}
