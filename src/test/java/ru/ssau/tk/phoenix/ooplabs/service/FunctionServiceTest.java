package ru.ssau.tk.phoenix.ooplabs.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.dto.*;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FunctionServiceTest {

    @Autowired
    private FunctionService functionService;
    @Autowired
    private UserService userService;

    private final FunctionDefinition simpleDefinition = new SimpleFunction("x^2", 11, 0, 10, null);
    private final FunctionDefinition tabulatedDefinition = new TabulatedFunction(null);


    @Test
    void findByFilter() {
        UserResponse user1 = userService.save(new UserRequest("user1", "123"));
        UserResponse user2 = userService.save(new UserRequest("user2", "123"));

        FunctionResponse func1 = functionService.save(new FunctionRequest(user1.getId(), "bac", FunctionType.SIMPLE, simpleDefinition));
        functionService.save(new FunctionRequest(user2.getId(), "aac", FunctionType.SIMPLE, simpleDefinition));
        functionService.save(new FunctionRequest(user1.getId(), "abc23", FunctionType.SIMPLE, simpleDefinition));
        functionService.save(new FunctionRequest(user1.getId(), "aaadsad", FunctionType.TABULATED, tabulatedDefinition));
        FunctionResponse func2 = functionService.save(new FunctionRequest(user1.getId(), "bacds", FunctionType.TABULATED, tabulatedDefinition));

        CompositeFunction compositeDefinition = new CompositeFunction(func1.getId(), func2.getId());
        functionService.save(new FunctionRequest(user1.getId(), "abcdft4o3tmf43mt", FunctionType.COMPOSITE, compositeDefinition));

        List<Criteria> filter = List.of(
                new Criteria("user_id", new Object[]{user1.getId()}, SortingType.ASC),
                new Criteria("name", null, SortingType.ASC));
        List<FunctionResponse> functions = functionService.findWithFilter(filter);
        assertEquals(5, functions.size());
        assertEquals("aaadsad", functions.get(0).getName());
        assertEquals("abcdft4o3tmf43mt", functions.get(2).getName());

        filter = List.of(new Criteria("user_id", new Object[]{user1.getId()}, null),
                new Criteria("name", null, SortingType.ASC),
                new Criteria("function_type", new Object[]{FunctionType.COMPOSITE, FunctionType.TABULATED}, SortingType.DESC));
        functions = functionService.findWithFilter(filter);
        assertEquals(3, functions.size());
        assertEquals(FunctionType.COMPOSITE, functions.get(1).getType());
        assertEquals("bacds", functions.get(2).getName());
    }

    @Test
    void findExistingFunction() {
        UserResponse user1 = userService.save(new UserRequest("user1", "123"));
        FunctionRequest request = new FunctionRequest(user1.getId(), "existingFunction", FunctionType.TABULATED, tabulatedDefinition);
        FunctionResponse saved = functionService.save(request);
        FunctionResponse found = functionService.find(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("existingFunction", found.getName());
        assertEquals(FunctionType.TABULATED, found.getType());
        assertEquals(user1.getId(), found.getUserId());

        assertInstanceOf(TabulatedFunction.class, found.getDefinition());
        assertEquals(saved.getDefinition().getPoints(), found.getDefinition().getPoints());
    }

    @Test
    void findWithFilterAndSorting() {
        UserResponse user1 = userService.save(new UserRequest("user1", "123"));
        UserResponse user2 = userService.save(new UserRequest("user2", "123"));

        FunctionResponse func1 = functionService.save(new FunctionRequest(user1.getId(), "Gamma", FunctionType.SIMPLE, simpleDefinition));
        FunctionResponse func2 = functionService.save(new FunctionRequest(user2.getId(), "Alpha", FunctionType.SIMPLE, simpleDefinition));
        CompositeFunction compositeDefinition = new CompositeFunction(func1.getId(), func2.getId());
        functionService.save(new FunctionRequest(user1.getId(), "Beta", FunctionType.COMPOSITE, compositeDefinition));
        functionService.save(new FunctionRequest(user1.getId(), "Delta", FunctionType.TABULATED, tabulatedDefinition));
        List<Criteria> filter = List.of(
                new Criteria("user_id", new Object[]{user1.getId()}, null),
                new Criteria("name", null, SortingType.ASC)
        );
        List<FunctionResponse> result = functionService.findWithFilter(filter);
        assertEquals(3, result.size());
        assertEquals("Beta", result.get(0).getName());
        assertEquals("Delta", result.get(1).getName());
        assertEquals("Gamma", result.get(2).getName());
    }

    @Test
    void updateFunction() {
        UserResponse user1 = userService.save(new UserRequest("user1", "123"));

        FunctionResponse original = functionService.save(new FunctionRequest(
                user1.getId(), "oldName", FunctionType.SIMPLE, simpleDefinition
        ));
        FunctionResponse updatedDto = new FunctionResponse(
                original.getId(),
                user1.getId(),
                "newName",
                FunctionType.TABULATED,
                tabulatedDefinition
        );
        functionService.update(updatedDto);
        FunctionResponse afterUpdate = functionService.find(original.getId());

        assertEquals(user1.getId(), afterUpdate.getUserId());
        assertEquals("newName", afterUpdate.getName());
        assertEquals(FunctionType.TABULATED, afterUpdate.getType());
        assertInstanceOf(TabulatedFunction.class, afterUpdate.getDefinition());
        assertEquals(updatedDto.getDefinition().getPoints(), afterUpdate.getDefinition().getPoints());
    }

    @Test
    void deleteFunction() {
        UserResponse user1 = userService.save(new UserRequest("user1", "123"));
        FunctionResponse saved = functionService.save(new FunctionRequest(
                user1.getId(), "iWantToLive", FunctionType.TABULATED, tabulatedDefinition
        ));
        functionService.delete(saved.getId());
        assertThrows(NoSuchElementException.class, () -> functionService.find(saved.getId()));
    }
    @Test
    void findNonExistingFunction() {
        assertThrows(NoSuchElementException.class, () -> functionService.find(999L));
    }
    @Test
    void updateNonExistingFunction() {
        FunctionResponse fake = new FunctionResponse(
                888L, 1L, "IDK", FunctionType.SIMPLE, simpleDefinition
        );
        assertThrows(NoSuchElementException.class, () -> functionService.update(fake));
    }
    @Test
    void deleteNonExistingFunction() {
        assertThrows(NoSuchElementException.class, () -> functionService.delete(888L));
    }
}
