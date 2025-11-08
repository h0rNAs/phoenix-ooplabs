/*
package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dao.UserDaoImpl;
import ru.ssau.tk.phoenix.ooplabs.dao.User;

import java.sql.Connection;
import java.util.Optional;

public class UserService {
    private final Connection conn;
    private final UserDaoImpl userDao;

    public UserService(Connection connection) {
        conn = connection;
        this.userDao = new UserDaoImpl(connection);
    }

    public Optional<User> findById(Long id){
        return userDao.findById(id);
    }

    public Optional<User> findByUsername(String username){
        return userDao.findByUsername(username);
    }

    public User create(User user){
        Optional<User> optionalUser = findByUsername(user.getUsername());
        if (optionalUser.isPresent()){
            throw new IllegalStateException("Пользователь с таким ником уже существует");
        }
        return userDao.save(user);
    }

    public void delete(Long id){
        Optional<User> optionalUser = findById(id);
        if (optionalUser.isEmpty()){
            throw new IllegalStateException("Пользователя с таким id не существует");
        }
        userDao.delete(id);
    }
}
*/
