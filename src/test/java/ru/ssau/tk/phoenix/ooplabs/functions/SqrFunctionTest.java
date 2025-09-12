package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqrFunctionTest {

    @Test
    void apply() {
        SqrFunction sqrFunction = new SqrFunction();
        assertEquals(25.,sqrFunction.apply(-5.));
        assertEquals(0.,sqrFunction.apply(0.));
        assertEquals(3.1415790025000003,sqrFunction.apply(1.77245));
        assertEquals(Double.MAX_VALUE,sqrFunction.apply(Double.MAX_VALUE-1000));
    }
}
