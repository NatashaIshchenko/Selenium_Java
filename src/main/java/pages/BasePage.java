package pages;

import driver.MainDriver;

public abstract class BasePage {
    /**
     * Домен, на котором находится страница
     */
    protected String domain;
    /**
     * Драйвер для работы со страницей
     */
    protected final MainDriver driver;
    /**
     * Инициализация драйвера без открытии сайта
     */
    protected BasePage(MainDriver driver) {
        this.driver = driver;
    }
    /**
     * Открытие главной страницы сайта
     *
     * @param driver Драйвер, с которым будет работать страница
     * @param domain Домен, на котором находится страница
     */
    protected BasePage(MainDriver driver, String domain) {
        this.driver = driver;
        this.domain = domain;
        driver.get(domain);
    }
    /**
     * Открытие сайта с относительным путем
     *
     * @param driver Драйвер, с которым будет работать страница
     * @param domain Домен, на котором находится страница
     * @param path   Относительный путь
     */
    protected BasePage(MainDriver driver, String domain, String path) {
        this.driver = driver;
        this.domain = domain;
        driver.get(getPageAddress(path));
    }
    /**
     * Получение абсолютного пути к странице
     *
     * @param path Путь к странице
     * @return Абсолютный путь к странице
     */
    protected final String getPageAddress(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(domain);
        if ((domain.endsWith("/")) && (path.startsWith("/"))) {
            sb.append(path.substring(1));
            return sb.toString();
        }
        if ((!domain.endsWith("/")) && (!path.startsWith("/"))) {
            sb.append("/");
        }
        sb.append(path);
        return sb.toString();
    }
}