package prototypes;

import db.DBConnection;
import driver.MainDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public abstract class  BaseTest {

   /* static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {   }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {   }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {  }
    }*/

    private static DBConnection connect;

    private MainDriver driver;

    /**
     * Поле для хранения эксепшина, с которым упал драйвер при инициализации
     */
    protected Throwable driverError = null;

    protected MainDriver getDriver() {
        return driver;
    }

    protected void setDriver(MainDriver driver) {
        this.driver = driver;
    }

    protected abstract String getProjectName();

    protected String getDomain() { return getProperty("domain"); }
    /**
     * Путь к папке скриншотов
     */
    private static final Path PATH_TO_SCREENSHOTS = Paths.get(getOutputDirectory(), "\\target\\screenshots\\");
    /**
     * Получение пути выходной директории
     * @return Путь к выходной директории
     */
    private static String getOutputDirectory() {
        String outputDirectory = System.getProperty("basedir");
        if (outputDirectory == null) {
            outputDirectory = System.getProperty("user.dir");
        }
        return outputDirectory;
    }

    //===========================

    /**
     * Получение пропертей в UTF-8 формате из файла <projectName>.properties
     */
    private Properties getPropertiesFile() {
        String propertiePath = "src/main/resources/" + getProjectName() + ".properties";
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(new File(propertiePath));
            prop.load(new InputStreamReader(fis, Charset.forName("UTF-8")));
        } catch (IOException e) {
            throw new Error("Property file \"" + propertiePath + "\" not found");
        }
        return prop;
    }

    /**
     * Получение значения для требуемого свойства
     * если в БД нет, то получает из файла *.properties
     *
     * @param name Имя свойства
     * @return Значение свойства
     */
    protected String getProperty(String name) {
        connect = DBConnection.getInstance();
        System.out.println("Basetest get property");
        System.out.println("projectName = " + getProjectName());
        String property = connect.getProperty(getProjectName(), name);
        if (property == null) {
            property = getPropertiesFile().getProperty(name);
        }
        if (property == null) {
            System.err.println("Property " + name + " is not set.");
            return "";
        }
        return property;
    }

    //===========================

    /**
     * Создание скриншотов после запуска теста и удаление лишних окон
     *
     * @param result Результат теста
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        Set<String> windows = driver.getWindowHandles();
        String mainWindow = driver.getWindowHandle();
        boolean isFailure = result.getStatus() == ITestResult.FAILURE;
        windows.remove(mainWindow);
        int i = 0;
        String filename = getClass().getSimpleName() + "_" + RandomStringUtils.randomAlphanumeric(10);
        if (isFailure) {
            getScreenshot(filename);
        }
        for (String window : windows) {
            driver.switchTo().window(window);
            if (isFailure) {
                getScreenshot(filename + "_" + ++i);
            }
            driver.close();
        }
        driver.switchTo().window(mainWindow);
        driver.switchTo().defaultContent();
    }
    /**
     * Создание скриншота текущего окна драйвера
     *
     * @param filename Имя файла, под которым сохранить скриншот
     */
    private void getScreenshot(String filename) {
        File scn = driver.getScreenshotAs(OutputType.FILE);
        File image = new File(PATH_TO_SCREENSHOTS.toFile(), filename + ".png");
        File html = new File(PATH_TO_SCREENSHOTS.toFile(), filename + ".html");
        try {
            String pageSource = replaceLinks();
            FileUtils.write(html, pageSource, StandardCharsets.UTF_8);
            Files.move(scn.toPath(), image.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod_2() {
        try {
            //TakesScreenshot ts = (TakesScreenshot) driver;
            //File source = ts.getScreenshotAs(OutputType.FILE);
            File source = driver.getScreenshotAs(OutputType.FILE);
            String filename = "method2" + "_" + RandomStringUtils.randomAlphanumeric(10);
            FileUtils.copyFile(source, new File(PATH_TO_SCREENSHOTS +"\\"+ filename + ".png"));
        } catch (Exception ex) {
            System.out.println("===== Error during screenshot ======");
        }

    }
    //===========================

    /**
     * Подготовка класса тестов к запуску:
     * - Получение инстанса драйвера для теста
     * - Расширение на весь экран
     */
    @BeforeClass(alwaysRun=true)
    public void beforeClass() {
        try {
            driver = MainDriver.getInstance();
            driver.manage().window().maximize();
        } catch (Throwable e){
            driverError = new Throwable(e);
        }
    }
    /**
     * Чистка кук
     */
    @BeforeMethod(alwaysRun=true)
    public void beforeMethod() {
        if (driver == null) {
            if (driverError != null)
                throw new Error(driverError);
            throw new SkipException("Driver is null");
        }
        driver.clearCookies();
        driver.implicitlyWaitSetDefault();
    }
    /**
     * Корректное завершение работы класса тестов:
     * - Закрытие драйвера после окончания всех методов в тесте
     *
     * @param context Контекст тестов
     */

    @AfterClass(alwaysRun = true)
    public void afterClass(ITestContext context) {
        if (driver != null) {
            driver.getWindowHandles().forEach(id -> driver.switchTo().window(id).close());
            driver.quit();
        }
    }

    @AfterSuite
    public void afterSuite() {
        DBConnection.close();
    }

    //====================

    /**
     * Метод для поиска и замены относительных ссылок на абсолютные
     */
    private String replaceLinks() {
        String url = driver.getCurrentUrl();
        Document doc = Jsoup.parse(driver.getPageSource(), url);
        doc.select("html").first().attr("pageUrl", url);
        doc.select("a[href],link[href]").forEach(e -> replaceLink(e, "href"));
        doc.select("script[src],img[src]").forEach(e -> replaceLink(e, "src"));
        return doc.html();
    }

    /**
     * Замена относительных ссылок абсолютными
     *
     * @param e         Элемент, в котором нужно заменить ссылку на относительную
     * @param attribute Аттрибут, в котором нужно сделать замену
     */
    private void replaceLink(Element e, String attribute) {
        String absUrl = e.absUrl(attribute);
        e.attr(attribute, absUrl);
    }

    //====================

}
