package ru.ssau.tk.phoenix.ooplabs.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.dto.CompositeFunction;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionDefinition;
import ru.ssau.tk.phoenix.ooplabs.dto.SimpleFunction;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class FunctionRepositoryTest {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;

    private final FunctionDefinition simpleDefinition = new SimpleFunction("simple", 11, 0, 10, null);


    @Test
    void creating_a_Function() {
        User user = new User("John_Doe", "password");
        User savedUser = userRepository.save(user);

        Function createdFunction = new Function(savedUser.getId(), "function_to_create", "SIMPLE", simpleDefinition);

        Function savedFunction = functionRepository.save(createdFunction);
        Long functionId = savedFunction.getId();
        assertTrue(functionRepository.existsById(functionId));
        assertInstanceOf(SimpleFunction.class, savedFunction.getDefinition());
    }

    @Test
    void create_CompositeFunction(){
        User user = new User("Test", "password");
        User savedUser = userRepository.save(user);

        Function func1 = functionRepository.save(
                new Function(savedUser.getId(), "func1", "SIMPLE", simpleDefinition));
        Function func2 = functionRepository.save(
                new Function(savedUser.getId(), "func2", "SIMPLE", simpleDefinition));

        CompositeFunction definition = new CompositeFunction(func1.getId(), func2.getId());
        Function createdFunction = new Function(savedUser.getId(), "", "COMPOSITE", definition);
        Function savedFunction = functionRepository.save(createdFunction);

        assertInstanceOf(CompositeFunction.class, savedFunction.getDefinition());
        Long id2 = ((CompositeFunction)savedFunction.getDefinition()).getId2();
        assertEquals("func2", functionRepository.findById(id2).get().getName());
    }

    @Test
    void finding_a_Function() {
        User user = new User("Ivan_Ivanov", "пароль");
        User savedUser = userRepository.save(user);

        Function checkFunction = new Function(savedUser.getId(), "function_to_check", "SIMPLE", simpleDefinition);
        Function savedFunction = functionRepository.save(checkFunction);

        Long functionId = savedFunction.getId();
        Optional<Function> foundFunction = functionRepository.findById(functionId);
        assertTrue(foundFunction.isPresent());
        assertEquals(functionId, foundFunction.get().getId());
        assertEquals("function_to_check", foundFunction.get().getName());
        assertEquals("SIMPLE", foundFunction.get().getType());
        assertEquals(savedUser.getId(), foundFunction.get().getUserId());
        assertInstanceOf(SimpleFunction.class, savedFunction.getDefinition());
    }

    @Test
    void deleting_a_Function() {
        User user = new User("Daniil_Korotkov", "2281337");
        User savedUser = userRepository.save(user);

        Function function = new Function(savedUser.getId(), "function_to_delete", "SIMPLE", simpleDefinition);
        Function savedFunction = functionRepository.save(function);
        Long functionId = savedFunction.getId();
        functionRepository.deleteById(functionId);
        assertFalse(functionRepository.existsById(functionId));
    }
}