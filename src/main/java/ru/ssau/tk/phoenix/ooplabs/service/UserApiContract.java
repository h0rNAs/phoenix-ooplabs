package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

public interface UserApiContract {
    UserResponse save(UserRequest request);
    UserResponse find(Long id);
    UserResponse find(String username);
    void update(Long id, String password);
    void delete(Long id);
}
