package ru.ssau.tk.phoenix.ooplabs.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDaoImplTest {
    private static Function FUNC_SIMPLE_1 = new Function(1L, Function.FunctionType.SIMPLE, "{}");
    private static Function FUNC_SIMPLE_3 = new Function(3L, Function.FunctionType.SIMPLE, "{}");
    private static Function FUNC_COMPOSITE_2 = new Function(2L, Function.FunctionType.SIMPLE, "{}");
    private static Function FUNC_TABULATED_1 = new Function(1L, Function.FunctionType.COMPOSITE, "{}");

    private static Function func_simple_2;

    private static final FunctionDaoImpl functionDao = DataBaseManager.getFunctionDao();


    @Test
    void testAll() throws SQLException {
        // Создание
        FUNC_SIMPLE_1 = functionDao.save(FUNC_SIMPLE_1);
        FUNC_SIMPLE_3 = functionDao.save(FUNC_SIMPLE_3);
        FUNC_COMPOSITE_2 = functionDao.save(FUNC_COMPOSITE_2);
        FUNC_TABULATED_1 = functionDao.save(FUNC_TABULATED_1);


        // Проверка id
        assertTrue(functionDao.findById(23423L).isEmpty());
        assertEquals(FUNC_SIMPLE_1.getId(), FUNC_COMPOSITE_2.getId() - 2L);
        assertEquals(FUNC_SIMPLE_1.getId(), FUNC_TABULATED_1.getId() - 3L);

        // Получение функций по userId
        List<Function> functions = functionDao.findByUserId(1L);
        assertEquals(2, functions.size());
        functions = functionDao.findByUserId(43L);
        assertTrue(functions.isEmpty());

        // Проверка получения данных
        assertEquals(FUNC_SIMPLE_1.getType(), functionDao.findById(FUNC_SIMPLE_1.getId()).get().getType());

        // Создал и удалил
        func_simple_2 = functionDao.save(new Function(2L, Function.FunctionType.SIMPLE, "{}"));
        Optional<Function> optionalSimpleFunc = functionDao.findById(func_simple_2.getId());
        assertTrue(optionalSimpleFunc.isPresent());
        assertEquals(Function.FunctionType.SIMPLE, optionalSimpleFunc.get().getType());

        functions = functionDao.findByUserId(2L);
        assertEquals(2, functions.size());

        functionDao.delete(func_simple_2.getId());
        assertTrue(functionDao.findById(func_simple_2.getId()).isEmpty());
        assertEquals(1, functionDao.findByUserId(2L).size());


        // Удаление
        functionDao.delete(FUNC_SIMPLE_1.getId());
        functionDao.delete(FUNC_COMPOSITE_2.getId());
        functionDao.delete(FUNC_SIMPLE_3.getId());
        functionDao.delete(FUNC_TABULATED_1.getId());
    }

    @AfterAll
    static void deleteTestFunctions() throws SQLException {
        Optional<Function> func_simple_1 = functionDao.findById(FUNC_SIMPLE_1.getId());
        if (func_simple_1.isPresent()) functionDao.delete(FUNC_SIMPLE_1.getId());

        Optional<Function> func_composite_2 = functionDao.findById(FUNC_COMPOSITE_2.getId());
        if (func_composite_2.isPresent()) functionDao.delete(FUNC_COMPOSITE_2.getId());

        Optional<Function> func_simple_3 = functionDao.findById(FUNC_SIMPLE_3.getId());
        if (func_simple_3.isPresent()) functionDao.delete(FUNC_SIMPLE_3.getId());

        Optional<Function> func_tabulated_1 = functionDao.findById(FUNC_TABULATED_1.getId());
        if (func_tabulated_1.isPresent()) functionDao.delete(FUNC_TABULATED_1.getId());
    }
}