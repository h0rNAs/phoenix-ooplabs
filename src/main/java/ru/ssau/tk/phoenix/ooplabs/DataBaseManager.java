package ru.ssau.tk.phoenix.ooplabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.service.FunctionApiContract;
import ru.ssau.tk.phoenix.ooplabs.service.FunctionService;
import ru.ssau.tk.phoenix.ooplabs.service.UserApiContract;
import ru.ssau.tk.phoenix.ooplabs.service.UserService;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDaoImpl;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDaoImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

public class DataBaseManager {
    private static Connection conn;
    private static UserApiContract userService;
    private static FunctionApiContract functionService;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;

    private final static Logger logger = LogManager.getLogger();


    private static boolean initialized = false;
    private static final Object lock = new Object();

    private static Properties props = new Properties();
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String adminUsername;
    private static String adminPassword;


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadConfiguration();
        ensureInitialized();
    }

    private static void loadConfiguration() {
        try (InputStream input = DataBaseManager.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                logger.error("Не удалось найти файл application.properties");
                throw new RuntimeException("Файл application.properties не найден в classpath");
            }

            props.load(input);

            dbUrl = getProperty("JDBC_URL", "db.url");
            dbUsername = getProperty("JDBC_USERNAME", "db.username");
            dbPassword = getProperty("JDBC_PASSWORD", "db.password");
            adminUsername = props.getProperty("admin.username");
            adminPassword = props.getProperty("admin.password");

            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                throw new RuntimeException("Отсутствуют обязательные параметры базы данных в application.properties");
            }

            logger.info("Конфигурация успешно загружена");
        } catch (IOException e) {
            logger.error("Ошибка загрузки конфигурации: {}", e.getMessage());
            throw new RuntimeException("Не удалось загрузить конфигурацию", e);
        }
    }

    public static void ensureInitialized() {
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    connectToDB();
                    initDB();

                    userDao = new UserDaoImpl(conn);
                    functionDao = new FunctionDaoImpl(conn);

                    userService = new UserService(userDao);
                    functionService = new FunctionService(functionDao, userDao);

                    initialized = true;
                    logger.info("БД инициализирована");
                }
            }
        }
    }

    public static void truncateTable(String name) throws SQLException {
        ensureInitialized();
        String sql = "TRUNCATE TABLE " + name + " RESTART IDENTITY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        logger.info("Произведен сброс таблицы БД. name = " + name);
    }

    private static void initDB() {
        try {
            String usersTable = loadResourceAsString("/scripts/users.sql");
            String functionsTable = loadResourceAsString("/scripts/functions.sql");
            String performancesTable = loadResourceAsString("/scripts/performances.sql");
            logger.info("Таблицы загружена");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(usersTable);
                stmt.execute(functionsTable);
                stmt.execute(performancesTable);
                logger.info("Таблицы sql успешно созданы");
            } catch (SQLException e) {
                logger.error("Ошибка создания таблицы sql: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            logger.error("Ошибка загрузки sql скриптов: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void connectToDB() {
        try {
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            logger.info("БД успешно подключена");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к БД: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String loadResourceAsString(String resourcePath) throws IOException {
        try (InputStream inputStream = DataBaseManager.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Ресурс не найден: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static String getAdminUsername() {
        return adminUsername;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static Properties getProps() {
        return new Properties(props);
    }

    @Deprecated
    public static UserDaoImpl getUserDao() {
        return userDao;
    }

    @Deprecated
    public static FunctionDaoImpl getFunctionDao() {
        return functionDao;
    }

    public static Connection getConnection() {
        return conn;
    }

    public static UserApiContract getUserService() {
        return userService;
    }

    public static FunctionApiContract getFunctionService() {
        return functionService;
    }

    public static String getProperty(String envVar, String propKey) {
        String value = System.getenv(envVar);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }

        value = props.getProperty(propKey);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }

        return "";
    }
}
