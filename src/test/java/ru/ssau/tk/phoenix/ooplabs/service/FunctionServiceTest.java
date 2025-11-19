package ru.ssau.tk.phoenix.ooplabs.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
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

    @Test
    void findByFilter() {
        functionService.save(new FunctionRequest(1L, "bac", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(2L, "aac", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(1L, "abc23", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(1L, "aaadsad", FunctionType.TABULATED, "{}"));
        functionService.save(new FunctionRequest(1L, "bacds", FunctionType.TABULATED, "{}"));
        functionService.save(new FunctionRequest(1L, "abcdft4o3tmf43mt", FunctionType.COMPOSITE, "{}"));

        List<Criteria> filter = List.of(
                new Criteria("user_id", new Object[]{1L}, SortingType.ASC),
                new Criteria("name", null, SortingType.ASC));
        List<FunctionResponse> functions = functionService.findWithFilter(filter);
        assertEquals(5, functions.size());
        assertEquals("aaadsad", functions.get(0).getName());
        assertEquals("abcdft4o3tmf43mt", functions.get(2).getName());

        filter = List.of(new Criteria("user_id", new Object[]{1L}, null),
                new Criteria("name", null, SortingType.ASC),
                new Criteria("function_type", new Object[]{FunctionType.COMPOSITE, FunctionType.TABULATED}, SortingType.DESC));
        functions = functionService.findWithFilter(filter);
        assertEquals(3, functions.size());
        assertEquals(FunctionType.COMPOSITE, functions.get(1).getType());
        assertEquals("bacds", functions.get(2).getName());
    }

    @Test
    void findExistingFunction() {
        FunctionRequest request = new FunctionRequest(1L, "existingFunction", FunctionType.TABULATED, "{}");
        FunctionResponse saved = functionService.save(request);
        FunctionResponse found = functionService.find(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("existingFunction", found.getName());
        assertEquals(FunctionType.TABULATED, found.getType());
        assertEquals(1L, found.getUserId());
    }

    @Test
    void findWithFilterAndSorting() {
        functionService.save(new FunctionRequest(1L, "Gamma", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(2L, "Alpha", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(1L, "Beta", FunctionType.COMPOSITE, "{}"));
        functionService.save(new FunctionRequest(1L, "Delta", FunctionType.TABULATED, "{}"));
        List<Criteria> filter = List.of(
                new Criteria("user_id", new Object[]{1L}, null),
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
        FunctionResponse original = functionService.save(new FunctionRequest(
                1L, "oldName", FunctionType.SIMPLE, "{}"
        ));
        FunctionResponse updatedDto = new FunctionResponse(
                original.getId(),
                1L,
                "newName",
                FunctionType.COMPOSITE,
                "{}"
        );
        functionService.update(updatedDto);
        FunctionResponse afterUpdate = functionService.find(original.getId());
        assertEquals(1L, afterUpdate.getUserId());
        assertEquals("newName", afterUpdate.getName());
        assertEquals(FunctionType.COMPOSITE, afterUpdate.getType());
        assertEquals("{}", afterUpdate.getDefinition());
    }

    @Test
    void deleteFunction() {
        FunctionResponse saved = functionService.save(new FunctionRequest(
                1L, "iWantToLive", FunctionType.TABULATED, "{}"
        ));
        functionService.delete(saved.getId());
        assertThrows(NoSuchElementException.class, () -> functionService.find(saved.getId()));
    }
}