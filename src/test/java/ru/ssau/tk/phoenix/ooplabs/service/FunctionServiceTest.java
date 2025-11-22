package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class FunctionServiceTest {
    private static final FunctionApiContract FUNCTION_SERVICE = DataBaseManager.getFunctionService();
    private static final UserApiContract USER_SERVICE = DataBaseManager.getUserService();

    @BeforeAll
    static void before() throws SQLException {
        DataBaseManager.truncateTable("users");
        DataBaseManager.getConnection().setAutoCommit(false);
    }

    @Test
    void testAll() throws SQLException {
        USER_SERVICE.save(new UserRequest("sasaf", "fsdfds"));
        USER_SERVICE.save(new UserRequest("sasasdfdf", "fsdfds"));

        // Создание
        FunctionResponse funcSimple1 = FUNCTION_SERVICE.save(
                new FunctionRequest(1L, "x^2", FunctionType.SIMPLE, "{}"));
        FunctionResponse funcComposite2 = FUNCTION_SERVICE.save(
                new FunctionRequest(2L, "my_func", FunctionType.COMPOSITE, "{}"));
        FunctionResponse funcTabulated1 = FUNCTION_SERVICE.save(
                new FunctionRequest(1L, "", FunctionType.COMPOSITE, "{}"));


        // Проверка id
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.find(23423L));
        assertEquals(funcSimple1.getId(), funcComposite2.getId() - 1L);
        assertEquals(funcSimple1.getId(), funcTabulated1.getId() - 2L);

        // Получение функций по userId
        List<FunctionResponse> functions = FUNCTION_SERVICE.findByUserId(1L);
        assertEquals(2, functions.size());
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.findByUserId(43L));

        // Проверка получения данных
        assertEquals(funcSimple1.getType(), FUNCTION_SERVICE.find(funcSimple1.getId()).getType());

        // Обновление
        funcComposite2.setName("my_new_func");
        funcComposite2.setDefinition("{\"function\": \"ln(sin(x))\"}");
        FUNCTION_SERVICE.update(funcComposite2);
        FunctionResponse newFuncComposite2 = FUNCTION_SERVICE.find(funcComposite2.getId());
        assertEquals("my_new_func", newFuncComposite2.getName());
        assertEquals("{\"function\": \"ln(sin(x))\"}", newFuncComposite2.getDefinition());
        assertEquals(newFuncComposite2.getUserId(), funcComposite2.getUserId());

        // Создал и удалил
        FunctionResponse func_simple_2 = FUNCTION_SERVICE.save(new FunctionRequest(2L, "lnx", FunctionType.SIMPLE, "{}"));
        FunctionResponse optionalSimpleFunc = FUNCTION_SERVICE.find(func_simple_2.getId());
        assertEquals(FunctionType.SIMPLE, optionalSimpleFunc.getType());

        functions = FUNCTION_SERVICE.findByUserId(2L);
        assertEquals(2, functions.size());

        FUNCTION_SERVICE.delete(func_simple_2.getId());
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.find(func_simple_2.getId()));
        assertEquals(1, FUNCTION_SERVICE.findByUserId(2L).size());
    }

    @Test
    void testFilter() throws SQLException {
        DataBaseManager.getConnection().rollback();

        USER_SERVICE.save(new UserRequest("sasaf", "fsdfds"));
        USER_SERVICE.save(new UserRequest("sasasdfdf", "fsdfds"));

        FUNCTION_SERVICE.save(new FunctionRequest(1L, "bac", FunctionType.SIMPLE, "{}"));
        FUNCTION_SERVICE.save(new FunctionRequest(2L, "aac", FunctionType.SIMPLE, "{}"));
        FUNCTION_SERVICE.save(new FunctionRequest(1L, "abc23", FunctionType.SIMPLE, "{}"));
        FUNCTION_SERVICE.save(new FunctionRequest(1L, "aaadsad", FunctionType.TABULATED, "{}"));
        FUNCTION_SERVICE.save(new FunctionRequest(1L, "bacds", FunctionType.TABULATED, "{}"));
        FUNCTION_SERVICE.save(new FunctionRequest(1L, "abcdft4o3tmf43mt", FunctionType.COMPOSITE, "{}"));

        List<Criteria> filter = List.of(new Criteria("user_id", new Object[]{1L}, null),
                new Criteria("name", null, SortingType.ASC));
        List<FunctionResponse> functions = FUNCTION_SERVICE.findWithFilter(filter);
        assertEquals(5, functions.size());
        assertEquals("aaadsad", functions.get(0).getName());
        assertEquals("abcdft4o3tmf43mt", functions.get(2).getName());

        filter = List.of(new Criteria("user_id", new Object[]{1L}, null),
                new Criteria("name", null, SortingType.ASC),
                new Criteria("function_type", new Object[]{FunctionType.COMPOSITE, FunctionType.TABULATED}, SortingType.DESC));
        functions = FUNCTION_SERVICE.findWithFilter(filter);
        assertEquals(3, functions.size());
        assertEquals(FunctionType.COMPOSITE, functions.get(1).getType());
        assertEquals("bacds", functions.get(2).getName());
    }

    @AfterAll
    static void after() throws SQLException {
        DataBaseManager.getConnection().rollback();
        DataBaseManager.getConnection().setAutoCommit(true);
    }
}
