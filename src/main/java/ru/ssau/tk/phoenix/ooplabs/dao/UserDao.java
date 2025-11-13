package ru.ssau.tk.phoenix.ooplabs.dao;

import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    Optional<UserResponse> findById(Long id) throws SQLException;
    Optional<UserResponse> findByUsername(String username) throws SQLException;
    UserResponse save(UserRequest user) throws SQLException;
    //void update(User user);
    void delete(Long id) throws SQLException;
}
