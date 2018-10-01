package driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.ProtocolHandshake;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

public class MainDriver implements WebDriver, TakesScreenshot {
    /**
     * Адрес Селениумгрид-хаба
     */
    private static final String REMOTE_WEBDRIVER_URL = "http://localhost:4444/wd/hub";
    /**
     * Инстанс вебдрайвера
     */
    private final WebDriver driver;

    /**
     * Дефолтный дебагстатус
     */
    private static final boolean DEFAULT_DEBUG_STATUS = true;
    /**
     * Константа для хранения статуса дебагмода
     */
    private static final boolean debug = getDebugStatus();
    /**
     * Значение инкремента для имплисити вейт по-умолчанию
     */
    private static final long DEFAULT_IMPLICITLY_WAIT_STEP = 3000;
    /**
     * Значение implicitly wait по-умолчанию
     */
    private static final long DEFAULT_IMPLICITLY_WAIT = 1000;
    /**
     * Значение задержки ожидания состояния вебдрайвера
     */
    private static final int DEFAULT_EXPECTED_CONDITION_WAIT = 60;
    /**
     * Текущий Implicitly Wait
     */
    private long currentImplicitlyWait = DEFAULT_IMPLICITLY_WAIT;

    private final WebDriverWait wait;
    /**
     * Конструктор для создания инстанса {@link MainDriver}
     *
     * @param driver Инстанс вебдрайвера
     */
    public MainDriver(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, DEFAULT_EXPECTED_CONDITION_WAIT);
    }
    /**
     * Выбор и получение инстанса вебдрайвера. В случае включенного дебагмода -
     * возвращается локальный вебдрайвер, выключенного - инстанс из селениум грида
     * А также установка уровня логирования для ProtocolHandshake
     * @return Экземпляр вебдрайвера
     */
    public static MainDriver getInstance() {
        Logger LOG = Logger.getLogger(ProtocolHandshake.class.getName());
        LOG.setLevel(Level.WARNING);
        WebDriver driver;
        if (debug) {
            driver = chooseLocalDriver();
        } else {
            driver = chooseRemoteDriver();
        }
        return new MainDriver(driver);
    }
    /**
     * Установка Implicitly Wait для драйвера
     *
     * @param ms Время ожидания в миллисекундах
     */
    public void implicitlyWaitSet(long ms) {
        currentImplicitlyWait = ms;
        manage().timeouts().implicitlyWait(ms, TimeUnit.MILLISECONDS);
    }
    /**
     * Повышение текущего Implicitly Wait для драйвера на {@link #DEFAULT_IMPLICITLY_WAIT_STEP}
     */
    public void implicitlyWaitIncrease() {
        implicitlyWaitSet(currentImplicitlyWait + DEFAULT_IMPLICITLY_WAIT_STEP);
    }
    /**
     * Повышение текущего Implicitly Wait для драйвера на {@link #DEFAULT_IMPLICITLY_WAIT_STEP}
     */
    public void implicitlyWaitSetDefault() {
        implicitlyWaitSet(DEFAULT_IMPLICITLY_WAIT);
    }
    /**
     * Получение класса для ожидания состояний
     *
     * @return Инстанс класс ожидания для Вебдрайвера
     */
    public WebDriverWait waitCondition() {
        return wait;
    }
    /**
     * Получение дебагстатуса из системных свойств. В случае отсутствия его там
     * - используется статус по умолчанию из {@link #DEFAULT_DEBUG_STATUS}
     *
     * @return Статус дебагмода. true - включён, false - выключен
     */
    private static boolean getDebugStatus() {
        String status = System.getProperty("debug");
        boolean debugStatus;
        if (status == null) {
            debugStatus = DEFAULT_DEBUG_STATUS;
        } else {
            debugStatus = Boolean.parseBoolean(status);
        }
        if (debugStatus) {
            ChromeDriverManager.getInstance(CHROME).setup();
        }
        return debugStatus;
    }
    /**
     * Удаление всех кук у драйвера
     */
    public void clearCookies() {
        manage().deleteAllCookies();
        navigate().refresh();
    }
    /**
     * Получение локального инстанса вебдрайвера
     *
     * @return Инстанс вебдрайвера
     */
    private static WebDriver chooseLocalDriver() {
        System.out.println("===== Local browser =====");
        ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().build();
        return new ChromeDriver(service);
    }
    /**
     * Получение удалённого инстанса вебдрайвера
     *
     * @return Инстанс вебдрайвера
     */
    private static WebDriver chooseRemoteDriver() {
        System.out.println("===== Remote browser =====");
        ChromeOptions capability = new ChromeOptions();
        try {
            RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL(REMOTE_WEBDRIVER_URL), capability);
            remoteDriver.setFileDetector(new LocalFileDetector());
            return remoteDriver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Incorrect URL of remote webdriver", e);
        }
    }

    @Override
    public Options manage() { return driver.manage(); }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public WebElement findElement(By arg0) { return driver.findElement(arg0); }

    @Override
    public List<WebElement> findElements(By arg0) { return driver.findElements(arg0); }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public void get(String arg0) { driver.get(arg0); }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    public Object executeScript(String arg0, Object... arg1) {
        return ((JavascriptExecutor) driver).executeScript(arg0, arg1);
    }

    /******************* Дополнительные методы ***********************/

    /**
     * Вставка текста в видимое поле для текста
     *
     * @param locator Локатор поля для ввода текста
     * @param text    Текст для вставки
     */
    public void enterTextToField(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Виден ли элемент
     *
     * @param locator Локатор элемента
     * @return true - виден, false - нет
     */
    public boolean isElementVisible(By locator) {
        int i = 0;
        do {
            try {
                return findElement(locator).isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            } catch (StaleElementReferenceException e) {
            }
        } while (i++ < 5);
        throw new StaleElementReferenceException("Can't validate element after " + i + " tries");
    }

    /**
     * Установка обычного чекбокса в нужное положение
     *
     * @param locator      Локатор самого инпута чекбокса
     * @param neededStatus Необходимый статус чекбокса
     */
    public void setCheckBoxStatus(By locator, boolean neededStatus) {
        WebElement checkBox = findElement(locator);
        if (checkBox.isSelected() != neededStatus) {
            checkBox.click();
        }
    }

    /**
     * Установка графически отрисованного чекбокса в нужное положение
     *
     * @param locator        Локатор самого инпута чекбокса
     * @param graphicLocator Локатор графического элемента для чекбокса
     * @param neededStatus   Необходимый статус чекбокса
     */
    public void setGraphicCheckBoxStatus(By locator, By graphicLocator, boolean neededStatus) {
        WebElement checkBox = findElement(locator);
        int i = 0;
        while ((checkBox.isSelected() != neededStatus) && (i++ < 2)) {
            checkBox.findElement(graphicLocator).click();
        }
    }

    /**
     * Медленный ввод текста в поле
     */
    public void lazyEnterTextToField(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     *  Возвращает лог из консоли браузера актуальный на момент вызова
     *  Вывод всего лога: for (LogEntry entry : getDriver().analyzeLog()) {System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());}
     */
    public LogEntries analyzeLog() {
        return driver.manage().logs().get(LogType.BROWSER);
    }
}