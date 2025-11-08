package ru.ssau.tk.phoenix.ooplabs.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(Long id) throws SQLException;
    Optional<User> findByUsername(String username) throws SQLException;
    User save(User user) throws SQLException;
    //void update(User user);
    void delete(Long id) throws SQLException;
}
