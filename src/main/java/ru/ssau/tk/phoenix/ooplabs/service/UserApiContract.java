package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

import java.sql.SQLException;

public interface UserApiContract {
    UserResponse save(UserRequest request) throws SQLException;
    boolean authenticate(String username, String password) throws SQLException;
    UserResponse find(Long id) throws SQLException;
    UserResponse find(String username) throws SQLException;
    void update(Long id, String password) throws SQLException;
    void delete(Long id) throws SQLException;
}