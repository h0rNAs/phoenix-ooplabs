package ru.ssau.tk.phoenix.ooplabs.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void savingUser() {
        UserRequest request = new UserRequest("John_Doe", "password");
        UserResponse response = userService.save(request);
        User savedUser = userRepository.findById(response.getId()).orElseThrow();
        assertEquals("John_Doe", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertNotNull(response.getId());
    }

    @Test
    void findingExistingUser() {
        User user = new User("Mary_Sue", "lucky");
        User saved = userRepository.save(user);
        UserResponse found = userService.find(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("Mary_Sue", found.getUsername());
    }

    @Test
    void findingExistingUserByUsername() {
        User user = new User("Pepsi", "Cola");
        userRepository.save(user);
        UserResponse found = userService.find("Pepsi");
        assertEquals("Pepsi", found.getUsername());
    }

    @Test
    void updatingUser() {
        User user = new User("Alice", "AliceTheBest");
        User saved = userRepository.save(user);
        userService.update(saved.getId(), "AliceAndBob4ever!");
        User updated = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Alice", updated.getUsername());
        assertEquals("AliceAndBob4ever!", updated.getPassword());
    }

    @Test
    void deletingExistingUser() {
        User user = new User("Bob", "IloveAlice");
        User saved = userRepository.save(user);
        userService.delete(saved.getId());
        assertFalse(userRepository.existsById(saved.getId()));
    }
}