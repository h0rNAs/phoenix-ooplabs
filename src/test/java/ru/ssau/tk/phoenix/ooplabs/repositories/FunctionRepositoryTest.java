package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FunctionRepositoryTest {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void creating_a_Function() {
        User user = new User("John_Doe", "password");
        User savedUser = userRepository.save(user);
        Function createdFunction = new Function(savedUser, "SIMPLE", "function_to_create", "{\"function\": \"x^2\", \"interval\": {\"from\": 0, \"to\": 10}, \"points_count\": 100}");
        Function savedFunction = functionRepository.save(createdFunction);
        Long functionId = savedFunction.getId();
        assertTrue(functionRepository.existsById(functionId));
    }

    @Test
    void finding_a_Function() {
        User user = new User("Ivan_Ivanov", "пароль");
        User savedUser = userRepository.save(user);
        Function checkFunction = new Function(savedUser, "SIMPLE", "function_to_check", "{\"function\": \"x^2\", \"interval\": {\"from\": 0, \"to\": 10}, \"points_count\": 100}");
        Function savedFunction = functionRepository.save(checkFunction);
        Long functionId = savedFunction.getId();
        Optional<Function> foundFunction = functionRepository.findById(functionId);
        assertTrue(foundFunction.isPresent());
        assertEquals(functionId, foundFunction.get().getId());
        assertEquals("function_to_check", foundFunction.get().getName());
        assertEquals("SIMPLE", foundFunction.get().getType());
        assertEquals(savedUser.getId(), foundFunction.get().getUser().getId());
    }
    @Test
    void deleting_a_Function() {
        User user = new User("Daniil_Korotkov", "2281337");
        User savedUser = userRepository.save(user);
        Function function = new Function(savedUser, "SIMPLE", "function_to_delete", "{\"function\": \"x^2\", \"interval\": {\"from\": 0, \"to\": 10}, \"points_count\": 100}");
        Function savedFunction = functionRepository.save(function);
        Long functionId = savedFunction.getId();
        functionRepository.deleteById(functionId);
        assertFalse(functionRepository.existsById(functionId));
    }
}