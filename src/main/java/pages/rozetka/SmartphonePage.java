package pages.rozetka;

import driver.MainDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import java.util.List;

public final class SmartphonePage extends BasePage {
    /**
     * Путь к  странице
     */
    private static final String PATH = "/mobile-phones/c80003/preset=smartfon/";

    private static final By LOAD_BUTTON = By.cssSelector(".preloader-animation");

    private static final By PHONE_BLOCK = By.cssSelector(".g-i-tile-catalog");

    private static final By ACTION = By.xpath(".//i[@class='g-tag g-tag-icon-middle-action sprite']");

    private static final By SUPER_PRICE = By.xpath(".//i[@class='g-tag g-tag-icon-middle-superprice sprite']");

    private static final By POPULAR = By.xpath(".//i[@class='g-tag g-tag-icon-middle-popularity sprite']");

    private static final By PHONE_NAME = By.xpath(".//div[@class='g-i-tile-i-title clearfix']/a");

    /**
     * Конструктор класса SmartphonePage
     */
    public SmartphonePage(MainDriver driver, String domain) {
        super(driver, domain, PATH);
    }

    public void getPhoneName(String label) {
        List<WebElement> phone_block_list = driver.findElements(PHONE_BLOCK);
        By label_locator;
        switch (label){
            case "action":
                label_locator = ACTION;
                break;
            case "super_price":
                 label_locator = SUPER_PRICE;
                 break;
            case "popular":
                 label_locator = POPULAR;
                 break;
            default:
                throw new IllegalArgumentException("Incorrect label parameter.");
        }
        for (WebElement element : phone_block_list) {
            if (element.findElements(label_locator).size() != 0)
                System.out.println(element.findElement(PHONE_NAME).getText());
        }
    }
}
