package ru.ssau.tk.phoenix.ooplabs.dao;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    private final UserDaoImpl userDao = DataBaseManager.getUserDao();

    @Test
    void findById() throws SQLException {
        assertEquals("xXoleg", userDao.findById(3L).get().getUsername());
        assertEquals("sergo", userDao.findById(1L).get().getPassword());
        assertTrue(userDao.findById(15L).isEmpty());
    }

    @Test
    void findByUsername() throws SQLException{
        assertEquals("j0rg2000", userDao.findByUsername("jorjo").get().getPassword());
        assertEquals(2L, userDao.findByUsername("_mary_").get().getId());
        assertTrue(userDao.findByUsername("asfdsgdfgregfg").isEmpty());
    }

    @Test
    void createAndDeleteUser () throws SQLException {
        User jorjo = userDao.findByUsername("jorjo").get();

        assertEquals("jorjo", userDao.findById(jorjo.getId()).get().getUsername());
        userDao.delete(jorjo.getId());
        assertTrue(userDao.findById(jorjo.getId()).isEmpty());
        assertTrue(userDao.findByUsername("jorjo").isEmpty());

        User dmitry = userDao.save(new User("dmitry", "0123dm1tro"));
        User olgaaa = userDao.save(new User("olgaaa", "o0olgaAa"));
        assertEquals(dmitry.getId() + 1L, olgaaa.getId());

        userDao.delete(userDao.findByUsername("dmitry").get().getId());
        userDao.delete(userDao.findByUsername("olgaaa").get().getId());
        assertTrue(userDao.findById(dmitry.getId()).isEmpty());
        assertTrue(userDao.findById(olgaaa.getId()).isEmpty());
        assertEquals("jorjo", userDao.save(jorjo).getUsername());
        assertEquals(olgaaa.getId() + 1L, userDao.findByUsername("jorjo").get().getId());
    }
}