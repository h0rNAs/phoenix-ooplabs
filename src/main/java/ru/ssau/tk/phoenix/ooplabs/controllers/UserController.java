package ru.ssau.tk.phoenix.ooplabs.controllers;

import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDao;
import ru.ssau.tk.phoenix.ooplabs.dao.UserDaoImpl;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserResponse save(UserRequest user) throws SQLException {
        return userDao.save(user);
    }

    public UserResponse find(Long id) throws SQLException {
        Optional<UserResponse> user = userDao.findById(id);
        if (user.isPresent()) return user.get();
        throw new NoSuchElementException("Пользователь не найден");
    }

    public UserResponse find(String username) throws SQLException {
        Optional<UserResponse> user = userDao.findByUsername(username);
        if (user.isPresent()) return user.get();
        throw new NoSuchElementException("Пользователь не найден");
    }

    public void delete(UserResponse user) throws SQLException {
        userDao.delete(user.getId());
    }
}
