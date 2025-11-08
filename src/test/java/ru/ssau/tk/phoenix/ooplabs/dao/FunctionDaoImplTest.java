package ru.ssau.tk.phoenix.ooplabs.dao;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDaoImplTest {
    private FunctionDaoImpl functionDao = DataBaseManager.getFunctionDao();

    @Test
    void findById() throws SQLException {
        Optional<Function> optionalFunction = functionDao.findById(2L);
        assertTrue(optionalFunction.isPresent());
        assertEquals(1L, optionalFunction.get().getUserId());
        assertEquals("{}", optionalFunction.get().getDefinition());

        assertEquals(Function.FunctionType.SIMPLE, functionDao.findById(4L).get().getType());

        assertTrue(functionDao.findById(2343L).isEmpty());
    }

    @Test
    void findByUserId() throws SQLException {
        List<Function> functions = functionDao.findByUserId(1L);
        assertEquals(2, functions.size());
        assertEquals(Function.FunctionType.COMPOSITE, functions.get(1).getType());

        assertTrue(functionDao.findByUserId(324L).isEmpty());
    }

    @Test
    void saveAndDelete() throws SQLException {
        Function simpleFunc = functionDao.save(new Function(3L, Function.FunctionType.SIMPLE, "{}"));
        Optional<Function> optionalSimpleFunc = functionDao.findById(simpleFunc.getId());
        assertTrue(optionalSimpleFunc.isPresent());
        assertEquals(Function.FunctionType.SIMPLE, optionalSimpleFunc.get().getType());
        assertEquals("{}", optionalSimpleFunc.get().getDefinition());

        List<Function> functions = functionDao.findByUserId(3L);
        assertTrue(functions.size() == 2);

        functionDao.delete(simpleFunc.getId());
        assertTrue(functionDao.findById(simpleFunc.getId()).isEmpty());
        assertTrue(functionDao.findByUserId(3L).size() == 1);
    }
}