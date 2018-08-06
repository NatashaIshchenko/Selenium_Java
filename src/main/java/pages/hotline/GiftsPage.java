package pages.hotline;

import driver.MainDriver;
import pages.BasePage;

public final class GiftsPage extends BasePage {
    /**
     * Путь к  странице
     */
    private static final String PATH = "/gifts/";
    /**
     * Конструктор класса GiftsPage
     */
    public GiftsPage(MainDriver driver, String domain) {
        super(driver, domain, PATH);
    }
}
