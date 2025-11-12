package ru.ssau.tk.phoenix.ooplabs.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    private static final User MARY = new User("mary02", "bl0dmary");
    private static final User JORJO = new User("jorjo", "j0rj2000");
    private static final User SERG = new User("serega228", "xXsergo_");
    private static  final User NIKITA = new User("n1kitaa", "__n1kit0s1k__");

    private static User dmitry;
    private static User olgaaa;

    private static final UserDaoImpl userDao = DataBaseManager.getUserDao();

    @Test
    void testAll() throws SQLException {
        // Создание
        User mary = userDao.save(MARY);
        User jorjo = userDao.save(JORJO);
        User serg = userDao.save(SERG);
        User nikita = userDao.save(NIKITA);

        // Проверка id
        assertTrue(userDao.findById(234324L).isEmpty());
        assertEquals(mary.getId(), jorjo.getId() - 1L);
        assertEquals(mary.getId() + 2L, serg.getId());

        // Проверка получения данных
        assertEquals("jorjo", userDao.findById(jorjo.getId()).get().getUsername());
        assertEquals("xXsergo_", userDao.findByUsername(serg.getUsername()).get().getPassword());

        // Создал и удалил
        dmitry = userDao.save(new User("dmitry", "0123dm1tro"));
        olgaaa = userDao.save(new User("olgaaa", "o0olgaAa"));
        assertEquals(dmitry.getId() + 1L, olgaaa.getId());
        userDao.delete(userDao.findByUsername("dmitry").get().getId());
        userDao.delete(userDao.findByUsername("olgaaa").get().getId());
        assertTrue(userDao.findById(dmitry.getId()).isEmpty());
        assertTrue(userDao.findById(olgaaa.getId()).isEmpty());

        // Удаление
        userDao.delete(mary.getId());
        userDao.delete(jorjo.getId());
        userDao.delete(serg.getId());
        userDao.delete(nikita.getId());
    }

    @AfterAll
    static void deleteTestUsers() throws SQLException {
        Optional<User> mary = userDao.findByUsername(MARY.getUsername());
        if (mary.isPresent()) userDao.delete(mary.get().getId());

        Optional<User> jorjo = userDao.findByUsername(JORJO.getUsername());
        if (jorjo.isPresent()) userDao.delete(jorjo.get().getId());

        Optional<User> serg = userDao.findByUsername(SERG.getUsername());
        if (serg.isPresent()) userDao.delete(serg.get().getId());

        Optional<User> nikita = userDao.findByUsername(NIKITA.getUsername());
        if (nikita.isPresent()) userDao.delete(nikita.get().getId());

        if (dmitry != null) userDao.delete(dmitry.getId());
        if (olgaaa != null) userDao.delete(olgaaa.getId());
    }
}