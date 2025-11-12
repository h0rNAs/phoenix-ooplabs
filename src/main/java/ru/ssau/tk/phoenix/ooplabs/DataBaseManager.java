package ru.ssau.tk.phoenix.ooplabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDaoImpl;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDaoImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DataBaseManager {
    private static Connection conn;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;


    private final static Logger logger = LogManager.getLogger();


    static {
        connectToDB();
        initDB();

        userDao = new UserDaoImpl(conn);
        functionDao = new FunctionDaoImpl(conn);

        logger.info("БД инициализирована");
    }

    public static void truncateTable(String name) throws SQLException {
        String sql = "TRUNCATE TABLE " + name + " RESTART IDENTITY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        logger.info("Произведен сброс таблицы БД. name = " + name);
    }

    private static void initDB(){
        try{
            String usersTable = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/users.sql")));
            String functionsTable = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/functions.sql")));
            String queryTable = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/query_performance.sql")));
            logger.info("Таблица загружена");
            try(Statement stmt = conn.createStatement()) {
                stmt.execute(usersTable);
                stmt.execute(functionsTable);
                stmt.execute(queryTable);
                logger.info("Таблицы sql успешно созданы");
            } catch (SQLException e) {
                logger.error("Ошибка создания таблицы sql: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            logger.error("Ошибка загрузки sql таблиц");
            throw new RuntimeException(e);
        }
    }

    private static void connectToDB(){
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(url, "postgres", "postgres");
            logger.info("БД успешно подключена");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к БД: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static UserDaoImpl getUserDao() {
        return userDao;
    }

    public static FunctionDaoImpl getFunctionDao() {
        return functionDao;
    }

    public static Connection getConnection() {
        return conn;
    }
}
