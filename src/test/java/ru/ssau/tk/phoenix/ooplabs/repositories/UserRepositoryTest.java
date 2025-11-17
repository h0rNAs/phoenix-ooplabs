package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.entities.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @Test
    void creating_a_User() {
        User user = new User("John_Doe", "password");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        assertTrue(userRepository.existsById(userId));
    }

    @Test
    void finding_a_User() {
        User user = new User("Ivan_Ivanov", "пароль");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        Optional<User> foundUser = userRepository.findById(userId);
        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getId());
        assertEquals("Ivan_Ivanov", foundUser.get().getUsername());
        assertEquals("пароль", foundUser.get().getPassword());
    }

    @Test
    void deleting_a_User() {
        User user = new User("Daniil_Korotkov", "2281337");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        userRepository.deleteById(userId);
        assertFalse(userRepository.existsById(userId));
    }
}