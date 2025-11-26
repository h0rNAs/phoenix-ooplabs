package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements UserApiContract {
    private final Logger logger = LogManager.getLogger(UserApiContract.class);
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse save(UserRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (!optionalUser.isEmpty())
            throw new IllegalArgumentException("Пользователь с username=" + request.getUsername() + " уже существует");

        User user = userRepository.save(mapRequestToEntity(request));
        logger.info("Пользователь id=" + user.getId() + " добавлен");
        return mapEntityToResponse(user);
    }

    @Override
    public boolean auth(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с username=" + username + " не найден");

        return password.equals(optionalUser.get().getPassword());
    }

    @Override
    public UserResponse find(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        logger.info("Пользователь id=" + optionalUser.get().getId() + " найден");
        return mapEntityToResponse(optionalUser.get());
    }

    @Override
    public UserResponse find(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с username=" + username + " не найден");

        logger.info("Пользователь username=" + optionalUser.get().getUsername() + " найден");
        return mapEntityToResponse(optionalUser.get());
    }

    @Override
    public UserResponse update(Long id, String password) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        User user = optionalUser.get();
        user.setPassword(password);

        logger.info("Пользователь id=" + optionalUser.get().getId() + " обновлен");
        return mapEntityToResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new NoSuchElementException("Пользователь с id=" + id + " не найден");

        logger.info("Пользователь id=" + optionalUser.get().getId() + " удален");
        userRepository.delete(optionalUser.get());
    }

    public static User mapRequestToEntity(UserRequest request) {
        return new User(request.getUsername(), request.getPassword());
    }

    public static UserResponse mapEntityToResponse(User entity){
        return new UserResponse(entity.getId(), entity.getUsername());
    }
}
