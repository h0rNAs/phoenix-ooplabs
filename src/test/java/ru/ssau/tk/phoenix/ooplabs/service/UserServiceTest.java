package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static final UserApiContract USER_SERVICE = DataBaseManager.getUserService();

    @Test
    void testAll() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        // Создание
        UserResponse mary = USER_SERVICE.save(
                new UserRequest("mary02", "bl0dmary"));
        UserResponse jorjo = USER_SERVICE.save(
                new UserRequest("jorjo", "j0rj2000"));
        UserResponse serg = USER_SERVICE.save(
                new UserRequest("serega228", "xXsergo_"));

        // Проверка id
        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find(234324L));
        assertEquals(mary.getId(), jorjo.getId() - 1L);
        assertEquals(mary.getId() + 2L, serg.getId());

        // Проверка получения данных
        assertEquals("jorjo", USER_SERVICE.find(jorjo.getId()).getUsername());

        // Создал и удалил
        UserResponse dmitry = USER_SERVICE.save(new UserRequest("dmitry", "0123dm1tro"));
        UserResponse olgaaa = USER_SERVICE.save(new UserRequest("olgaaa", "o0olgaAa"));
        assertEquals(dmitry.getId() + 1L, olgaaa.getId());
        USER_SERVICE.delete(USER_SERVICE.find("dmitry").getId());
        USER_SERVICE.delete(USER_SERVICE.find("olgaaa").getId());
        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find(dmitry.getId()));
        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find(olgaaa.getId()));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }
}