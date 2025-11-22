package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService implements UserApiContract {
    private final Logger logger = LogManager.getLogger(UserApiContract.class);
    private final UserDao userDao;


    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserResponse save(UserRequest request) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findByUsername(request.getUsername());
        if (optionalUser.isPresent())
            throw new IllegalArgumentException("Пользователь с username=" + request.getUsername() + " уже существует");

        UserResponse user = userDao.save(request);
        logger.info("Пользователь username=" + user.getUsername() + " добавлен");
        return user;
    }

    @Override
    public boolean authenticate(String username, String password) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с username=" + username + " не найден");

        boolean auth = userDao.authenticate(username, password);
        logger.info("Результат авторизации: {}", auth);
        return auth;
    }

    @Override
    public UserResponse find(Long id) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        logger.info("Пользователь id=" + optionalUser.get().getId() + " найден");
        return optionalUser.get();
    }

    @Override
    public UserResponse find(String username) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с username=" + username + " не найден");

        logger.info("Пользователь username=" + optionalUser.get().getUsername() + " найден");
        return optionalUser.get();
    }

    @Override
    public void update(Long id, String password) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        userDao.update(id, password);
        logger.info("Пользователь id=" + optionalUser.get().getId() + " обновлен");
    }

    @Override
    public void delete(Long id) throws SQLException {
        Optional<UserResponse> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        userDao.delete(id);
        logger.info("Пользователь id=" + optionalUser.get().getId() + " удален");
    }
}
