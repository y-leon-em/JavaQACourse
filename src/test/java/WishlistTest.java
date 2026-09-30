import annotations.BrowserMode;
import annotations.BrowserMode.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RegistrationPage;
import pages.WishListPage;
import utils.TestDataGenerator;

public class WishlistTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(WishlistTest.class);

    private static String userName;
    private static String userEmail;
    private static String userPassword;
    private static boolean userRegistered = false;

    @BeforeEach
    void loginBeforeTest() {
        if (!userRegistered) {
            userName = TestDataGenerator.randomName();
            userEmail = TestDataGenerator.randomEmail();
            userPassword = TestDataGenerator.randomPassword();

            new RegistrationPage(driver)
                    .open()
                    .typeUserName(userName)
                    .typeUserEmail(userEmail)
                    .typeUserPassword(userPassword)
                    .clickRegistrationButton()
                    .waitForUrlContains("/login");

            userRegistered = true;
            log.info("Пользователь зарегистрирован: {}", userName);
        }

        new LoginPage(driver)
                .open()
                .typeUserName(userName)
                .typeUserPassword(userPassword)
                .clickLoginButton()
                .waitForUrlContains("/wishlists");
    }

    @Test
    @DisplayName("Создание нового списка желаний")
    @BrowserMode(Mode.MAXIMIZED)
    void createNewWishlist() {
        final String title = TestDataGenerator.randomWishlistTitle();
        final String description = TestDataGenerator.randomWishlistDescription();

        WishListPage page = new WishListPage(driver)
                .createWishlist(title, description);

        String uuid = page.getUuidByName(title);
        page.assertCardWithUuidIsPresent(uuid)
                .assertCardTitleIs(uuid, title)
                .assertCardDescriptionIs(uuid, description)
                .assertCardGiftCountIs(uuid, "0 подарков");

        log.info("Тест пройден успешно");
    }

    @Test
    @DisplayName("Удаление списка желаний")
    @BrowserMode(Mode.MAXIMIZED)
    void deleteWishlist() {
        final String title = TestDataGenerator.randomWishlistTitle();

        WishListPage page = new WishListPage(driver)
                .createWishlist(title, "временное описание");

        String uuid = page.getUuidByName(title);
        page.assertCardWithUuidIsPresent(uuid)
                .deleteByUuid(uuid)
                .assertCardWithUuidIsAbsent(uuid);

        log.info("Тест пройден успешно");
    }

    @Test
    @DisplayName("Отмена создания списка не создаёт карточку")
    @BrowserMode(Mode.MAXIMIZED)
    void cancelCreatingWishlist() {
        final String title = TestDataGenerator.randomWishlistTitle();

        WishListPage page = new WishListPage(driver)
                .openNewWishlistDialog()
                .typeNewListTitle(title)
                .typeNewListDescription("описание")
                .cancelCreatingWishlist();

        page.assertCardWithNameIsAbsent(title);

        log.info("Тест пройден успешно");
    }

}
