package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionTest {

    @Test
    void interpolate() {
        MockTabulatedFunction func = new MockTabulatedFunction();
        assertEquals(17.5, func.interpolate(250, 200, 300, 15, 20));
        assertEquals(55, func.interpolate(1000, 200, 300, 15, 20)); //extrapolate
        assertEquals(17.5, func.interpolate(250, 300, 200, 20, 15)); //границы наоборот
    }

    @Test
    void apply_extrapolate() {
        double[] xValues = new double[]{200, 300};
        double[] yValues = new double[]{15, 20};
        MockTabulatedFunction func = new MockTabulatedFunction(xValues, yValues);
        assertEquals(55, func.apply(1000));
        assertEquals(4.5, func.apply(-10));
    }

    @Test
    void apply_interpolate() {
        double[] xValues = new double[]{200, 300};
        double[] yValues = new double[]{15, 20};
        MockTabulatedFunction func = new MockTabulatedFunction(xValues, yValues);
        assertEquals(17.5, func.apply(250));
        assertEquals(15, func.apply(200));
    }

    @Test
    public void testIteratorWithWhileLoop() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        Iterator<Point> iterator = function.iterator();
        int index = 0;// Тестирование цикла while
        while (((Iterator<?>) iterator).hasNext()) {
            Point point = iterator.next();
            assertEquals(xValues[index], point.x, 1e-9);
            assertEquals(yValues[index], point.y, 1e-9);
            index++;
        }
        assertEquals(3, index); // Проверяем, что прошли все 3 элемента
    }

    @Test
    public void testIteratorWithForEachLoop() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        int index = 0; // Тестирование цикла for-each
        for (Point point : function) {
            assertEquals(xValues[index], point.x, 1e-9);
            assertEquals(yValues[index], point.y, 1e-9);
            index++;
        }
        assertEquals(3, index);
    }
}