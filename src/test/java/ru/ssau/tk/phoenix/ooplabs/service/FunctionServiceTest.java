/*
package ru.ssau.tk.phoenix.ooplabs.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.DataBaseManager;
import ru.ssau.tk.phoenix.ooplabs.dao.Function;

import static org.junit.jupiter.api.Assertions.*;

class FunctionServiceTest {
    private static FunctionService functionService = DataBaseManager.getFunctionService();
    private static final Logger logger = LogManager.getLogger(FunctionService.class);

    @Test
    void findById() {
        assertEquals(Function.FunctionType.SIMPLE, functionService.findById(1L).get().getType());
        assertEquals(true, functionService.findById(1242L).isEmpty());
    }
}*/
