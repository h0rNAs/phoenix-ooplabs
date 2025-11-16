package ru.ssau.tk.phoenix.ooplabs.controllers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionResponse;
import ru.ssau.tk.phoenix.ooplabs.util.Criteria;
import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;
import ru.ssau.tk.phoenix.ooplabs.util.SortingType;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class FunctionControllerTest {
    private static final FunctionController functionController = DataBaseManager.getFunctionController();

    @BeforeAll
    static void before() throws SQLException {
        DataBaseManager.getConnection().setAutoCommit(false);
    }

    @Test
    void testAll() throws SQLException {
        // Создание
        FunctionResponse funcSimple1 = functionController.save(
                new FunctionRequest(1L, "x^2", FunctionType.SIMPLE, "{}"));
        FunctionResponse funcComposite2 = functionController.save(
                new FunctionRequest(2L, "my_func", FunctionType.COMPOSITE, "{}"));
        FunctionResponse funcTabulated1 = functionController.save(
                new FunctionRequest(1L, "", FunctionType.COMPOSITE, "{}"));


        // Проверка id
        assertThrows(NoSuchElementException.class, () -> functionController.find(23423L));
        assertEquals(funcSimple1.getId(), funcComposite2.getId() - 1L);
        assertEquals(funcSimple1.getId(), funcTabulated1.getId() - 2L);

        // Получение функций по userId
        List<FunctionResponse> functions = functionController.findByUserId(1L);
        assertEquals(2, functions.size());
        functions = functionController.findByUserId(43L);
        assertTrue(functions.isEmpty());

        // Проверка получения данных
        assertEquals(funcSimple1.getType(), functionController.find(funcSimple1.getId()).getType());

        // Создал и удалил
        FunctionResponse func_simple_2 = functionController.save(new FunctionRequest(2L, "lnx", FunctionType.SIMPLE, "{}"));
        FunctionResponse optionalSimpleFunc = functionController.find(func_simple_2.getId());
        assertEquals(FunctionType.SIMPLE, optionalSimpleFunc.getType());

        functions = functionController.findByUserId(2L);
        assertEquals(2, functions.size());

        functionController.delete(func_simple_2);
        assertThrows(NoSuchElementException.class, () -> functionController.find(func_simple_2.getId()));
        assertEquals(1, functionController.findByUserId(2L).size());
    }

    @Test
    void testFilter() throws SQLException {
        DataBaseManager.getConnection().rollback();

        functionController.save(new FunctionRequest(1L, "bac", FunctionType.SIMPLE, "{}"));
        functionController.save(new FunctionRequest(2L, "aac", FunctionType.SIMPLE, "{}"));
        functionController.save(new FunctionRequest(1L, "abc23", FunctionType.SIMPLE, "{}"));
        functionController.save(new FunctionRequest(1L, "aaadsad", FunctionType.TABULATED, "{}"));
        functionController.save(new FunctionRequest(1L, "bacds", FunctionType.TABULATED, "{}"));
        functionController.save(new FunctionRequest(1L, "abcdft4o3tmf43mt", FunctionType.COMPOSITE, "{}"));

        List<Criteria> filter = List.of(new Criteria("name", null, SortingType.ASC));
        List<FunctionResponse> functions = functionController.findAndSortWithFilter(1L, filter);
        assertEquals(5, functions.size());
        assertEquals("aaadsad", functions.get(0).getName());
        assertEquals("abcdft4o3tmf43mt", functions.get(2).getName());

        filter = List.of(new Criteria("name", null, SortingType.ASC),
                new Criteria("function_type", new Object[]{FunctionType.COMPOSITE, FunctionType.TABULATED}, SortingType.DESC));
        functions = functionController.findAndSortWithFilter(1L, filter);
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
