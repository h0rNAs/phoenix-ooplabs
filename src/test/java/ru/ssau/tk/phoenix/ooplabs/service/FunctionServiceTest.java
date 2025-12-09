package ru.ssau.tk.phoenix.ooplabs.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.*;
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
    private final FunctionDefinition simpleDefinition = new SimpleFunction("x^2", 11, 0, 10, null);
    private final FunctionDefinition tabulatedDefinition = new TabulatedFunction(null);

    @BeforeAll
    static void before() throws SQLException {
        DataBaseManager.truncateTable("users");
        DataBaseManager.getConnection().setAutoCommit(false);
    }

    @Test
    void testAll() throws SQLException {
        UserResponse user1 = USER_SERVICE.save(new UserRequest("sasaf", "fsdfds"));
        UserResponse user2 = USER_SERVICE.save(new UserRequest("sasasdfdf", "fsdfds"));

        // Создание
        FunctionResponse funcSimple1 = FUNCTION_SERVICE.save(
                new FunctionRequest(user1.getId(), "x^2", FunctionType.SIMPLE, simpleDefinition));
        CompositeFunction compositeDefinition = new CompositeFunction(funcSimple1.getId(), null);
        FunctionResponse funcComposite2 = FUNCTION_SERVICE.save(
                new FunctionRequest(user2.getId(), "my_func", FunctionType.COMPOSITE, compositeDefinition));
        FunctionResponse funcTabulated1 = FUNCTION_SERVICE.save(
                new FunctionRequest(user1.getId(), "composite", FunctionType.COMPOSITE, compositeDefinition));


        // Проверка id
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.find(23423L));
        assertEquals(funcSimple1.getId(), funcComposite2.getId() - 1L);
        assertEquals(funcSimple1.getId(), funcTabulated1.getId() - 2L);

        // Получение функций по userId
        List<FunctionResponse> functions = FUNCTION_SERVICE.findByUserId(user1.getId());
        assertEquals(2, functions.size());
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.findByUserId(43L));

        // Проверка получения данных
        assertEquals(funcSimple1.getType(), FUNCTION_SERVICE.find(funcSimple1.getId()).getType());

        // Обновление
        compositeDefinition = new CompositeFunction(null, funcSimple1.getId());
        funcComposite2.setName("my_new_func");
        funcComposite2.setDefinition(compositeDefinition);
        FUNCTION_SERVICE.update(funcComposite2);
        FunctionResponse newFuncComposite2 = FUNCTION_SERVICE.find(funcComposite2.getId());
        assertEquals("my_new_func", newFuncComposite2.getName());
        assertEquals(compositeDefinition.getId2(), ((CompositeFunction)newFuncComposite2.getDefinition()).getId2());
        assertEquals(newFuncComposite2.getUserId(), funcComposite2.getUserId());

        // Создал и удалил
        FunctionResponse func_simple_2 = FUNCTION_SERVICE.save(
                new FunctionRequest(user2.getId(), "lnx", FunctionType.SIMPLE, simpleDefinition));
        FunctionResponse optionalSimpleFunc = FUNCTION_SERVICE.find(func_simple_2.getId());
        assertEquals(FunctionType.SIMPLE, optionalSimpleFunc.getType());

        functions = FUNCTION_SERVICE.findByUserId(user2.getId());
        assertEquals(2, functions.size());

        FUNCTION_SERVICE.delete(func_simple_2.getId());
        assertThrows(NoSuchElementException.class, () -> FUNCTION_SERVICE.find(func_simple_2.getId()));
        assertEquals(1, FUNCTION_SERVICE.findByUserId(user2.getId()).size());
    }

    @Test
    void testFilter() throws SQLException {
        DataBaseManager.getConnection().rollback();

        UserResponse user1 = USER_SERVICE.save(new UserRequest("sasaf", "fsdfds"));
        UserResponse user2 = USER_SERVICE.save(new UserRequest("sasasdfdf", "fsdfds"));

        FunctionResponse func1 = FUNCTION_SERVICE.save(new FunctionRequest(user1.getId(), "bac", FunctionType.SIMPLE, simpleDefinition));
        FunctionResponse func2 = FUNCTION_SERVICE.save(new FunctionRequest(user2.getId(), "aac", FunctionType.SIMPLE, simpleDefinition));
        CompositeFunction compositeDefinition = new CompositeFunction(func1.getId(), func2.getId());
        FUNCTION_SERVICE.save(new FunctionRequest(user1.getId(), "abc23", FunctionType.SIMPLE, simpleDefinition));
        FUNCTION_SERVICE.save(new FunctionRequest(user1.getId(), "aaadsad", FunctionType.TABULATED, tabulatedDefinition));
        FUNCTION_SERVICE.save(new FunctionRequest(user1.getId(), "bacds", FunctionType.TABULATED, tabulatedDefinition));
        FUNCTION_SERVICE.save(new FunctionRequest(user1.getId(), "abcdft4o3tmf43mt", FunctionType.COMPOSITE, compositeDefinition));

        List<Criteria> filter = List.of(new Criteria("user_id", new Object[]{user1.getId()}, null),
                new Criteria("name", null, SortingType.ASC));
        List<FunctionResponse> functions = FUNCTION_SERVICE.findWithFilter(filter);
        assertEquals(5, functions.size());
        assertEquals("aaadsad", functions.get(0).getName());
        assertEquals("abcdft4o3tmf43mt", functions.get(2).getName());

        filter = List.of(new Criteria("user_id", new Object[]{user1.getId()}, null),
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
