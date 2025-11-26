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
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private static Properties config = new Properties();
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String adminUsername;
    private static String adminPassword;


    static {
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

            config.load(input);

            dbUrl = config.getProperty("db.url");
            dbUsername = config.getProperty("db.username");
            dbPassword = config.getProperty("db.password");
            adminUsername = config.getProperty("admin.username");
            adminPassword = config.getProperty("admin.password");

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
            String url = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(url, "postgres", "postgres");
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

    public static Properties getConfig() {
        return new Properties(config);
    }

    @Deprecated
    public static UserDaoImpl getUserDao() {
        ensureInitialized();
        return userDao;
    }

    @Deprecated
    public static FunctionDaoImpl getFunctionDao() {
        ensureInitialized();
        return functionDao;
    }

    public static Connection getConnection() {
        ensureInitialized();
        return conn;
    }

    public static UserApiContract getUserService() {
        //ensureInitialized();
        return userService;
    }

    public static FunctionApiContract getFunctionService() {
        //ensureInitialized();
        return functionService;
    }
}
