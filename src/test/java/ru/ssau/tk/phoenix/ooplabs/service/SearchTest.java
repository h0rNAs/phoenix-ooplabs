package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.entities.Function;
import ru.ssau.tk.phoenix.ooplabs.entities.User;
import ru.ssau.tk.phoenix.ooplabs.repositories.FunctionRepository;
import ru.ssau.tk.phoenix.ooplabs.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchTest {

    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SearchService search;

    @Test
    void findFunctionByIdAndOrderByNameAsc() {
        User user = new User("John_Doe", "password");
        User saved = userRepository.save(user);
        functionRepository.save(new Function(saved, "SIMPLE", "Gamma", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Alpha", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Beta", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameAsc(saved.getId());
        assertEquals(3, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Beta", result.get(1).getName());
        assertEquals("Gamma", result.get(2).getName());
    }

    @Test
    void findFunctionByIdAndOrderByNameDesc() {
        User user = new User("Robert", "password");
        User saved = userRepository.save(user);
        functionRepository.save(new Function(saved, "SIMPLE", "Alpha", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Gamma", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Beta", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameDesc(saved.getId());
        assertEquals(3, result.size());
        assertEquals("Gamma", result.get(0).getName());
        assertEquals("Beta", result.get(1).getName());
        assertEquals("Alpha", result.get(2).getName());
    }

    @Test
    void findFunctionByIdAndOrderByTypeAsc() {
        User user = new User("Robertson", "password");
        User saved = userRepository.save(user);
        functionRepository.save(new Function(saved, "TABULATED", "X", null));
        functionRepository.save(new Function(saved, "COMPOSITE", "Y", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Z", null));
        List<Function> result = search.findFunctionByIdAndOrderByTypeAsc(saved.getId());
        assertEquals(3, result.size());
        assertEquals("COMPOSITE", result.get(0).getType());
        assertEquals("SIMPLE", result.get(1).getType());
        assertEquals("TABULATED", result.get(2).getType());
    }

    @Test
    void findFunctionByIdAndOrderByTypeDesc() {
        User user = new User("Vis", "password");
        User saved = userRepository.save(user);
        functionRepository.save(new Function(saved, "SIMPLE", "A", null));
        functionRepository.save(new Function(saved, "TABULATED", "B", null));
        functionRepository.save(new Function(saved, "COMPOSITE", "C", null));
        List<Function> result = search.findFunctionByIdAndOrderByTypeDesc(saved.getId());
        assertEquals(3, result.size());
        assertEquals("TABULATED", result.get(0).getType());
        assertEquals("SIMPLE", result.get(1).getType());
        assertEquals("COMPOSITE", result.get(2).getType());
    }

    @Test
    void findFunctionByIdAndOrderByNameAndTypeAsc() {
        User user = new User("Kal_Al", "password");
        User saved = userRepository.save(user);
        String type = "SIMPLE";
        functionRepository.save(new Function(saved, "SIMPLE", "Gamma", null));
        functionRepository.save(new Function(saved, "TABULATED", "Ignore", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Alpha", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Delta", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameAndTypeAsc(saved.getId(), type);
        assertEquals(3, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Delta", result.get(1).getName());
        assertEquals("Gamma", result.get(2).getName());
    }

    @Test
    void findFunctionByIdAndOrderByNameAndTypeDesc() {
        User user = new User("Superman", "password");
        User saved = userRepository.save(user);
        String type = "SIMPLE";
        functionRepository.save(new Function(saved, "SIMPLE", "Alpha", null));
        functionRepository.save(new Function(saved, "COMPOSITE", "Ignore", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Gamma", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Beta", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameAndTypeDesc(saved.getId(), type);
        assertEquals(3, result.size());
        assertEquals("Gamma", result.get(0).getName());
        assertEquals("Beta", result.get(1).getName());
        assertEquals("Alpha", result.get(2).getName());
    }

    @Test
    void findFunctionByIdAndOrderByNameAndTypeInAsc() {
        User user = new User("Eminem", "password");
        User saved = userRepository.save(user);
        List<String> types = Arrays.asList("SIMPLE", "TABULATED");
        functionRepository.save(new Function(saved, "SIMPLE", "Gamma", null));
        functionRepository.save(new Function(saved, "TABULATED", "Alpha", null));
        functionRepository.save(new Function(saved, "COMPOSITE", "IgnoreMe", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Delta", null));
        functionRepository.save(new Function(saved, "TABULATED", "Epsilon", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameAndTypeInAsc(saved.getId(), types);
        assertEquals(4, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Delta", result.get(1).getName());
        assertEquals("Epsilon", result.get(2).getName());
        assertEquals("Gamma", result.get(3).getName());
    }
    @Test
    void findFunctionByIdAndOrderByNameAndTypeInDesc() {
        User user = new User("Slim_Shady", "password");
        User saved = userRepository.save(user);
        List<String> types = Arrays.asList("SIMPLE", "TABULATED");
        functionRepository.save(new Function(saved, "SIMPLE", "Alpha", null));
        functionRepository.save(new Function(saved, "TABULATED", "Gamma", null));
        functionRepository.save(new Function(saved, "COMPOSITE", "Ignore", null));
        functionRepository.save(new Function(saved, "SIMPLE", "Delta", null));
        functionRepository.save(new Function(saved, "TABULATED", "Epsilon", null));
        List<Function> result = search.findFunctionByIdAndOrderByNameAndTypeInDesc(saved.getId(), types);
        assertEquals(4, result.size());
        assertEquals("Gamma", result.get(0).getName());
        assertEquals("Epsilon", result.get(1).getName());
        assertEquals("Delta", result.get(2).getName());
        assertEquals("Alpha", result.get(3).getName());
    }
}