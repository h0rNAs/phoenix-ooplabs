package ru.ssau.tk.phoenix.ooplabs.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionFactoryTest {

    @Test
    void create_LinkedList() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        assertInstanceOf(LinkedListTabulatedFunction.class, new LinkedListTabulatedFunctionFactory().create(xValues, yValues));
    }

    @Test
    void create_Array() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        assertInstanceOf(ArrayTabulatedFunction.class, new ArrayTabulatedFunctionFactory().create(xValues, yValues));
    }
}