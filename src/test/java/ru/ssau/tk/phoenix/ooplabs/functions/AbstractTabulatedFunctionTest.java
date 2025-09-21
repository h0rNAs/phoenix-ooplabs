package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionTest {

    @Test
    void interpolate() {
        MockTabulatedFunction func = new MockTabulatedFunction();
        assertEquals(17.5, func.interpolate(250, 200, 300, 15,20));
        assertEquals(55, func.interpolate(1000, 200, 300, 15,20)); //extrapolate
        assertEquals(17.5, func.interpolate(250, 300, 200, 20,15)); //границы наоборот
    }

    @Test
    void apply_extrapolate() {
        double[] xValues = new double[] {200, 300};
        double[] yValues = new double[] {15, 20};
        MockTabulatedFunction func = new MockTabulatedFunction(xValues, yValues);
        assertEquals(55, func.apply(1000));
        assertEquals(4.5, func.apply(-10));
    }

    @Test
    void apply_interpolate() {
        double[] xValues = new double[] {200, 300};
        double[] yValues = new double[] {15, 20};
        MockTabulatedFunction func = new MockTabulatedFunction(xValues, yValues);
        assertEquals(17.5, func.apply(250));
        assertEquals(15, func.apply(200));
    }
}