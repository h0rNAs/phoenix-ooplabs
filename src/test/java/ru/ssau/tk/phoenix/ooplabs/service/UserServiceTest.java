package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static final UserService USER_SERVICE = DataBaseManager.getUserController();

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

    @Test
    void createUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        UserResponse user = USER_SERVICE.save(new UserRequest("Alice", "password"));

        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        assertEquals("Alice", user.getUsername());

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void findById() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        UserResponse saved = USER_SERVICE.save(new UserRequest("Bob", "password"));
        UserResponse found = USER_SERVICE.find(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Bob", found.getUsername());

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void findByIdNonExistingUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find(999L));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void findByUsername() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        USER_SERVICE.save(new UserRequest("John_Doe", "password"));
        UserResponse found = USER_SERVICE.find("John_Doe");

        assertEquals("John_Doe", found.getUsername());

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void findByUsernameNonExistingUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find("nonexist"));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void updateNonExistingUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.update(888L, "password"));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void deleteUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        UserResponse user = USER_SERVICE.save(new UserRequest("Adam", "password"));
        Long id = user.getId();

        USER_SERVICE.delete(id);

        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.find(id));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }

    @Test
    void deleteNonExistingUser() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);

        assertThrows(NoSuchElementException.class, () -> USER_SERVICE.delete(777L));

        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }
}