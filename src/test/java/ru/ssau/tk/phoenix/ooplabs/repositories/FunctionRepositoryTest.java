package ru.ssau.tk.phoenix.ooplabs.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FunctionRepositoryTest {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void creating_a_Function() {
        User user = new User("John_Doe", "password");
        User savedUser = userRepository.save(user);

        Function createdFunction = new Function(savedUser.getId(), "function_to_create", "SIMPLE", "{}");

        Function savedFunction = functionRepository.save(createdFunction);
        Long functionId = savedFunction.getId();
        assertTrue(functionRepository.existsById(functionId));
    }

    @Test
    void finding_a_Function() {
        User user = new User("Ivan_Ivanov", "пароль");
        User savedUser = userRepository.save(user);

        Function checkFunction = new Function(savedUser.getId(), "function_to_check", "SIMPLE", "{}");
        Function savedFunction = functionRepository.save(checkFunction);

        Long functionId = savedFunction.getId();
        Optional<Function> foundFunction = functionRepository.findById(functionId);
        assertTrue(foundFunction.isPresent());
        assertEquals(functionId, foundFunction.get().getId());
        assertEquals("function_to_check", foundFunction.get().getName());
        assertEquals("SIMPLE", foundFunction.get().getType());
        assertEquals(savedUser.getId(), foundFunction.get().getUserId());
    }

    @Test
    void deleting_a_Function() {
        User user = new User("Daniil_Korotkov", "2281337");
        User savedUser = userRepository.save(user);

        Map<String, Object> definition = new HashMap<>();
        definition.put("function", "x^2");
        definition.put("interval", Map.of("from", 0f, "to", 10f));
        definition.put("points_count", 100);
        Function function = new Function(savedUser.getId(), "function_to_delete", "SIMPLE", "{}");
        Function savedFunction = functionRepository.save(function);
        Long functionId = savedFunction.getId();
        functionRepository.deleteById(functionId);
        assertFalse(functionRepository.existsById(functionId));
    }
}