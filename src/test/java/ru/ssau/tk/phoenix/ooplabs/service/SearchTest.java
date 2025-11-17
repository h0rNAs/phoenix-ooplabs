package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchTest {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Search search;
    @Test
    void searchByUserIdSortedByNameAsc() {
        User user = new User("John_Doe", "password");
        User savedUser = userRepository.save(user);
        Function function1 = new Function(savedUser, "SIMPLE", "Gamma", null);
        Function function2 = new Function(savedUser, "SIMPLE", "Alpha", null);
        Function function3 = new Function(savedUser, "SIMPLE", "Beta", null);
        functionRepository.save(function1);
        functionRepository.save(function2);
        functionRepository.save(function3);
        List<Function> result = search.searchByUserIdSortedByNameAsc(savedUser.getId());
        assertEquals(3, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Beta", result.get(1).getName());
        assertEquals("Gamma", result.get(2).getName());
    }
}