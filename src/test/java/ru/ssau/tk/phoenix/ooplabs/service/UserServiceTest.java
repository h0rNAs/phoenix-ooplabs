package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.dao.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {
    private static Connection conn;
    private static UserService userService;
    private static final Logger logger = LogManager.getLogger(UserServiceTest.class);

    @Test
    void findById() {
        assertEquals("xXoleg", userService.findById(3L).get().getUsername());
        assertEquals("sergo", userService.findById(1L).get().getPassword());
        assertEquals(true, userService.findById(15L).isEmpty());
    }

    @Test
    void findByUsername(){
        assertEquals("j0rg2000", userService.findByUsername("jorjo").get().getPassword());
        assertEquals(2L, userService.findByUsername("_mary_").get().getId());
        assertEquals(true, userService.findByUsername("asfdsgdfgregfg").isEmpty());
    }

    @Test
    void createAndDeleteUser(){
        User jorjo = userService.findByUsername("jorjo").get();

        assertEquals("jorjo", userService.findById(jorjo.getId()).get().getUsername());
        userService.delete(jorjo.getId());
        assertEquals(true, userService.findById(jorjo.getId()).isEmpty());
        assertEquals(true, userService.findByUsername("jorjo").isEmpty());

        User dmitry = userService.create(new User("dmitry", "0123dm1tro"));
        User olgaaa = userService.create(new User("olgaaa", "o0olgaAa"));
        assertEquals(dmitry.getId() + 1L, olgaaa.getId());

        userService.delete(userService.findByUsername("dmitry").get().getId());
        userService.delete(userService.findByUsername("olgaaa").get().getId());
        assertEquals(true, userService.findById(dmitry.getId()).isEmpty());
        assertEquals(true, userService.findById(olgaaa.getId()).isEmpty());
        assertEquals("jorjo", userService.create(jorjo).getUsername());
        assertEquals(olgaaa.getId() + 1L, userService.findByUsername("jorjo").get().getId());
    }

    @BeforeAll
    public static void InitDB(){
        connectToDB();

        try{
            String sql = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/scripts/users.sql")));
            logger.info("Таблица users.sql загружена");
            try(Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                logger.info("Таблица sql успешно создана");
            } catch (SQLException e) {
                logger.error("Ошибка создания таблицы sql: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            logger.error("Ошибка загрузки sql");
            throw new RuntimeException(e);
        }

        userService = new UserService(conn);

        logger.info("БД успешно инициализирована");
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
}