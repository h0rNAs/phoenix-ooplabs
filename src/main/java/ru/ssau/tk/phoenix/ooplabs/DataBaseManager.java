package ru.ssau.tk.phoenix.ooplabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dao.FunctionDaoImpl;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDaoImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
    private static Connection conn;
    /*private static UserService userService;
    private static FunctionService functionService;*/
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;


    private final static Logger logger = LogManager.getLogger();


    static {
        connectToDB();
        initDB();

        //userService = new UserService(conn);
        //functionService = new FunctionService(conn);
        userDao = new UserDaoImpl(conn);
        functionDao = new FunctionDaoImpl(conn);

        logger.info("БД инициализирована");
    }

    private static void initDB(){
        try{
            String usersTable = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/users.sql")));
            String functionsTable = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/functions.sql")));
            logger.info("Таблица загружена");
            try(Statement stmt = conn.createStatement()) {
                stmt.execute(usersTable);
                stmt.execute(functionsTable);
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

    /*public static UserService getUserService() {
        return userService;
    }

    public static FunctionService getFunctionService() {
        return functionService;
    }*/

    public static UserDaoImpl getUserDao() {
        return userDao;
    }

    public static FunctionDaoImpl getFunctionDao() {
        return functionDao;
    }
}
