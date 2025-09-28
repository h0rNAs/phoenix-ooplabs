package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrictTabulatedFunctionTest {

    @Test
    void apply_LinkedList(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strict = new StrictTabulatedFunction(func);
        assertEquals(2, strict.apply(2));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(3));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }

    @Test
    void apply_Array(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strict = new StrictTabulatedFunction(func);
        assertEquals(2, strict.apply(2));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(3));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }
}