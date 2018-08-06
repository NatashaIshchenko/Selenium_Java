package db;

import java.sql.*;

public class DBConnection {
    /**
     * база данных
     */
    private static final String DB_NAME = "testdb1";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
    private static final String DB_PARAMS = "?serverTimezone=UTC&autoReconnect=true&useSSL=false";
    /**
     * Логин к базе данных
     */
    private static final String DB_USER = "root";
    /**
     * Пароль к базе данных
     */
    private static final String DB_PASSWORD = "root";
    /**
     * Кол-во попыток для подключения
     */
    private static final int TRIES_LIMIT = 5;
    /**
     * Единственный инстанс данного класса
     */
    private static DBConnection connection;
    /**
     * Инстанс подключения к базе данных
     */
    private static Connection dbConnection;
    /**
     * Метод для получения единственного экземпляра данного класса
     * @return Инстанс данного класса
     */
    public static DBConnection getInstance() {
        synchronized (DBConnection.class) {
            if (connection == null) {
                connection = new DBConnection();
            }
        }
        return connection;
    }
    /**
     * Конструктор для создания единственного инстанса класса
     */
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception  e) {
            throw new Error(e);
        }
        connect();
    }
    /**
     * Метод для проверки подключения к базе данных и реконнекту к ней
     */
    private static void checkConnection() {
        try {
            if (dbConnection == null || dbConnection.isClosed())
                connect();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * Метод для подключения к базе данных
     */
    private static void connect() {
        int tries = 0;
        do {
            try {
                dbConnection = DriverManager.getConnection(DB_URL+DB_PARAMS, DB_USER, DB_PASSWORD);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                tries++;
            }
        } while (tries <= TRIES_LIMIT);
        throw new Error("Can't connect to database " + DB_URL + " after " + (--tries) + " tries");
    }
    /**
     * Закрытие соединения с базой данных
     */
    public static void close() {
        synchronized (DBConnection.class) {
            if (dbConnection == null) {
                return;
            }
            try {
                dbConnection.close();
                dbConnection = null;
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * получение проперти из БД
     */
    public String getProperty(String projectName, String propertyName) {
        String query = "SELECT value from " + DB_NAME + "." + projectName + " WHERE name = \"" + propertyName +"\";";
		String result = null;
		try {
			result = getFirstResult(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result == null) {
			System.err.println("Property \"" + propertyName + "\" is not set for " + projectName + " project");
		}
		return result;
    }
    /**
     * Выполнение запроса вида SELECT к базе данных
     * @param query Текст запроса
     * @return Результат запроса
     * @throws SQLException Эксепшн, связанный с текстом запроса и т.п.
     */
    private ResultSet executeQuery(CharSequence query) throws SQLException {
        checkConnection();
        Statement statement = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(query.toString());
    }
    /**
     * Получение результата из первой строки первого столбца из результата запроса
     * @param query Текст запроса
     * @return Первое значение из первого столбца первой строки
     * @throws SQLException Эксепшн, связанный с текстом запроса и т.п.
     */
    private String getFirstResult(CharSequence query) throws SQLException {
        ResultSet rs = executeQuery(query);
        if (!rs.first()) {
            rs.getStatement().close();
            rs.close();
            return null;
        }
        String result = rs.getString(1);
        rs.getStatement().close();
        rs.close();
        return result;
    }
}