package ru.ssau.tk.phoenix.ooplabs.dao;

import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    UserResponse save(UserRequest user) throws SQLException;
    boolean authenticate(String username, String password) throws SQLException;
    Optional<UserResponse> findById(Long id) throws SQLException;
    Optional<UserResponse> findByUsername(String username) throws SQLException;
    UserResponse update(Long id, String password) throws SQLException;
    void delete(Long id) throws SQLException;
}
