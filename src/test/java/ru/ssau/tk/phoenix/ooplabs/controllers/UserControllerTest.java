package ru.ssau.tk.phoenix.ooplabs.controllers;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private static final UserController userController = DataBaseManager.getUserController();

    @Test
    void testAll() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        // Создание
        UserResponse mary = userController.save(
                new UserRequest("mary02", "bl0dmary"));
        UserResponse jorjo = userController.save(
                new UserRequest("jorjo", "j0rj2000"));
        UserResponse serg = userController.save(
                new UserRequest("serega228", "xXsergo_"));

        // Проверка id
        assertThrows(NoSuchElementException.class, () -> userController.find(234324L));
        assertEquals(mary.getId(), jorjo.getId() - 1L);
        assertEquals(mary.getId() + 2L, serg.getId());

        // Проверка получения данных
        assertEquals("jorjo", userController.find(jorjo.getId()).getUsername());
        assertEquals("xXsergo_", userController.find(serg.getUsername()).getPassword());

        // Создал и удалил
        UserResponse dmitry = userController.save(new UserRequest("dmitry", "0123dm1tro"));
        UserResponse olgaaa = userController.save(new UserRequest("olgaaa", "o0olgaAa"));
        assertEquals(dmitry.getId() + 1L, olgaaa.getId());
        userController.delete(userController.find("dmitry"));
        userController.delete(userController.find("olgaaa"));
        assertThrows(NoSuchElementException.class, () -> userController.find(dmitry.getId()));
        assertThrows(NoSuchElementException.class, () -> userController.find(olgaaa.getId()));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }
}