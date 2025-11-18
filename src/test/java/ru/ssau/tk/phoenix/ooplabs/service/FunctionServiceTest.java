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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FunctionServiceTest {

    @Autowired private FunctionService functionService;

    @Test
    @Transactional
    void findByFilter() {
        functionService.save(new FunctionRequest(1L, "bac", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(2L, "aac", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(1L, "abc23", FunctionType.SIMPLE, "{}"));
        functionService.save(new FunctionRequest(1L, "aaadsad", FunctionType.TABULATED, "{}"));
        functionService.save(new FunctionRequest(1L, "bacds", FunctionType.TABULATED, "{}"));
        functionService.save(new FunctionRequest(1L, "abcdft4o3tmf43mt", FunctionType.COMPOSITE, "{}"));

        List<Criteria> filter = List.of(new Criteria("user_id", new Object[]{1L}, SortingType.ASC),
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
}