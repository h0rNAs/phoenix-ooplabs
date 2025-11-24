package ru.ssau.tk.phoenix.ooplabs.service;

import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;

public interface UserApiContract {
    UserResponse save(UserRequest request);
    boolean auth(String username, String password);
    UserResponse find(Long id);
    UserResponse find(String username);
    UserResponse update(Long id, String password);
    void delete(Long id);
}
