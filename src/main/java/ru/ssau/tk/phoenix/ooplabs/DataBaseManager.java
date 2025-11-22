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

public class DataBaseManager {
    private static Connection conn;
    private static UserApiContract userService;
    private static FunctionApiContract functionService;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;

    private final static Logger logger = LogManager.getLogger();


    private static boolean initialized = false;

    private static final Object lock = new Object();


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
        ensureInitialized();
        return userService;
    }

    public static FunctionApiContract getFunctionService() {
        ensureInitialized();
        return functionService;
    }
}
