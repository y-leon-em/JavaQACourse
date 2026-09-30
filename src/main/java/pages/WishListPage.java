package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class WishListPage extends BasePage {
    private static final String PAGE_PATH = "/wishlists";

    private static final By WISHLIST_HEADER = By.xpath("//*[text()='Мои списки желаний']");
    private static final By CREATE_NEW_LIST_BUTTON = By.xpath("//button[normalize-space()='Создать новый список']");
    private static final By ALL_CARDS = By.cssSelector(".card");
    // модальное окно создания списка
    private static final By DIALOG = By.cssSelector("*[class^=modal-dialog]");
    private static final By LIST_TITLE_INPUT =By.cssSelector("*[class^=modal-dialog] input[type='text']");
    private static final By LIST_DESCRIPTION_INPUT = By.cssSelector("*[class^=modal-dialog] textarea");
    private static final By DIALOG_CANCEL_BUTTON = By.cssSelector("*[class^=modal-dialog] button[type='button']");
    private static final By DIALOG_CREATE_BUTTON = By.cssSelector("*[class^=modal-dialog] button[type='submit']");

    private static final String CARD_BY_NAME =
            "//div[contains(@class, 'card')][.//div[contains(@class, 'card-title') and normalize-space()='%s']]";
    private static final String CARD_BY_UUID =
            "//div[contains(@class, 'card')][.//a[contains(@href, '%s')]]";

    private static final String TITLE_WITHIN = ".//div[contains(@class, 'card-title')]";
    private static final String DESCRIPTION_WITHIN = ".//p[contains(@class, 'card-text')][1]";
    private static final String GIFT_COUNT_WITHIN = ".//small[contains(@class, 'text-muted')]";
    private static final String VIEW_LINK_WITHIN = ".//a[contains(@href, '/wishlists/')]";
    private static final String DELETE_BUTTON_WITHIN = ".//button[contains(@class, 'btn-danger')]";

    public WishListPage(WebDriver driver) {super(driver);}

    public String getUrl() {
        return getCurrentUrl();
    }

    @Override
    public String getPath() {
        return PAGE_PATH;
    }


    private Boolean wishListHeaderIsDisplayed() {
        WebElement element = waitVisible(WISHLIST_HEADER);
        return element.isDisplayed();
    }

    /**
     * Открывает модальное окно создания нового списка.
     *
     */
    public WishListPage openNewWishlistDialog() {
        waitVisible(CREATE_NEW_LIST_BUTTON).click();
        waitVisible(DIALOG);
        return this;
    }

    /**
     * Вводит название нового списка в модальном окне.
     *
     * @param text название списка
     */
    public WishListPage typeNewListTitle(String text) {
        type(LIST_TITLE_INPUT, text);
        return this;
    }

    /**
     * Вводит описание нового списка в модальном окне.
     *
     * @param text описание списка
     */
    public WishListPage typeNewListDescription(String text) {
        type(LIST_DESCRIPTION_INPUT, text);
        return this;
    }

    /**
     * Отменяет создание списка и закрывает модальное окно.
     *
     */
    public WishListPage cancelCreatingWishlist() {
        waitVisible(DIALOG_CANCEL_BUTTON).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DIALOG));
        return this;
    }

    /**
     * Подтверждает создание нового списка в модальном окне.
     *
     */
    public WishListPage createNewWishlist() {
        waitVisible(DIALOG_CREATE_BUTTON).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DIALOG));
        return this;
    }

    /**
     * Полный сценарий создания нового списка желаний: открытие модального окна,
     * ввод названия и описания, подтверждение, ожидание появления карточки.
     *
     * @param title       название списка
     * @param description описание списка
     */
    public WishListPage createWishlist(String title, String description) {
        openNewWishlistDialog()
                .typeNewListTitle(title)
                .typeNewListDescription(description)
                .createNewWishlist();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(CARD_BY_NAME, title))));

        return this;
    }

    /**
     * Возвращает количество карточек списков на странице.
     *
     * @return количество карточек
     */
    public int getCardCount() {
        return driver.findElements(ALL_CARDS).size();
    }

    /**
     * Проверяет, присутствует ли карточка с указанным UUID.
     *
     * @param uuid идентификатор списка
     * @return {@code true}, если карточка найдена
     */
    public boolean isCardWithUuidPresent(String uuid) {
        return !driver.findElements(By.xpath(String.format(CARD_BY_UUID, uuid))).isEmpty();
    }

    /**
     * Проверяет, присутствует ли карточка с указанным названием.
     *
     * @param name название списка
     * @return {@code true}, если карточка найдена
     */
    public boolean isCardWithNamePresent(String name) {
        return !driver.findElements(By.xpath(String.format(CARD_BY_NAME, name))).isEmpty();
    }

    /**
     * Извлекает UUID списка из href карточки с указанным названием.
     * Возвращает UUID первой найденной карточки.
     *
     * @param title название списка
     * @return UUID списка
     */
    public String getUuidByName(String title) {
        WebElement card = waitVisible(By.xpath(String.format(CARD_BY_NAME, title)));
        String href = card.findElement(By.xpath(VIEW_LINK_WITHIN)).getAttribute("href");
        return href.substring(href.lastIndexOf('/') + 1);
    }

    /**
     * Возвращает название карточки по UUID списка.
     *
     * @param uuid идентификатор списка
     * @return название списка
     */
    public String getTitleByUuid(String uuid) {
        return waitVisible(By.xpath(String.format(CARD_BY_UUID, uuid)))
                .findElement(By.xpath(TITLE_WITHIN))
                .getText();
    }

    /**
     * Возвращает описание карточки по UUID списка.
     *
     * @param uuid идентификатор списка
     * @return описание списка
     */
    public String getDescriptionByUuid(String uuid) {
        return waitVisible(By.xpath(String.format(CARD_BY_UUID, uuid)))
                .findElement(By.xpath(DESCRIPTION_WITHIN))
                .getText();
    }

    /**
     * Возвращает текст счётчика подарков карточки по UUID списка.
     *
     * @param uuid идентификатор списка
     * @return текст счётчика (например, "0 подарков")
     */
    public String getGiftCountByUuid(String uuid) {
        return waitVisible(By.xpath(String.format(CARD_BY_UUID, uuid)))
                .findElement(By.xpath(GIFT_COUNT_WITHIN))
                .getText();
    }

    /**
     * Удаляет список по его UUID и ждёт исчезновения карточки со страницы.
     *
     * @param uuid идентификатор списка
     */
    public WishListPage deleteByUuid(String uuid) {
        By card = By.xpath(String.format(CARD_BY_UUID, uuid));
        waitVisible(card)
                .findElement(By.xpath(DELETE_BUTTON_WITHIN))
                .click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(card));
        return this;
    }

    /**
     * Проверяет, что заголовок страницы "Мои списки желаний" отображается.
     *
     */
    public WishListPage assertHeaderIsDisplayed() {
        assertTrue(wishListHeaderIsDisplayed(),
                "Заголовок страницы 'Мои списки желаний' не отображается");
        return this;
    }


    /**
     * Проверяет, что произошёл редирект на страницу списков желаний.
     *
     */
    public WishListPage assertRedirectOnPage() {
        assertTrue(getUrl().contains(getPath()),
                "Ожидался редирект на " + getPath() + ", но URL: " + getUrl());
        return this;
    }

    /**
     * Проверяет, что карточка с указанным UUID присутствует на странице.
     *
     * @param uuid идентификатор списка
     */
    public WishListPage assertCardWithUuidIsPresent(String uuid) {
        assertTrue(isCardWithUuidPresent(uuid),
                "Список с UUID " + uuid + " не найден");
        return this;
    }

    /**
     * Проверяет, что карточка с указанным UUID отсутствует на странице.
     *
     * @param uuid идентификатор списка
     */
    public WishListPage assertCardWithUuidIsAbsent(String uuid) {
        assertFalse(isCardWithUuidPresent(uuid),
                "Список с UUID " + uuid + " всё ещё присутствует");
        return this;
    }

    /**
     * Проверяет, что название карточки с указанным UUID совпадает с ожидаемым.
     *
     * @param uuid     идентификатор списка
     * @param expected ожидаемое название
     */
    public WishListPage assertCardTitleIs(String uuid, String expected) {
        assertEquals(expected, getTitleByUuid(uuid),
                "Название карточки не совпадает");
        return this;
    }

    /**
     * Проверяет, что описание карточки с указанным UUID совпадает с ожидаемым.
     *
     * @param uuid     идентификатор списка
     * @param expected ожидаемое описание
     */
    public WishListPage assertCardDescriptionIs(String uuid, String expected) {
        assertEquals(expected, getDescriptionByUuid(uuid),
                "Описание карточки не совпадает");
        return this;
    }

    /**
     * Проверяет, что счётчик подарков карточки с указанным UUID совпадает с ожидаемым.
     *
     * @param uuid     идентификатор списка
     * @param expected ожидаемый текст счётчика
     */
    public WishListPage assertCardGiftCountIs(String uuid, String expected) {
        assertEquals(expected, getGiftCountByUuid(uuid),
                "Счётчик подарков не совпадает");
        return this;
    }

    /**
     * Проверяет, что карточка с указанным названием отсутствует на странице.
     * Используется для проверки, что отмена создания не добавила список.
     *
     * @param name название списка
     */
    public WishListPage assertCardWithNameIsAbsent(String name) {
        assertFalse(isCardWithNamePresent(name),
                "Список добавляется при отмене создания");
        return this;
    }

}
