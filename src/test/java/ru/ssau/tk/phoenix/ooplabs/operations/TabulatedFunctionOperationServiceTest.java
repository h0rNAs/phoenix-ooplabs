package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TabulatedFunctionOperationServiceTest {

    @Test
    void testAsPointsWithValidFunction() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {2.0, 4.0, 6.0, 8.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        assertEquals(4, points.length);
        assertEquals(1.0, points[0].x, 1e-10);
        assertEquals(2.0, points[0].y, 1e-10);
        assertEquals(2.0, points[1].x, 1e-10);
        assertEquals(4.0, points[1].y, 1e-10);
        assertEquals(3.0, points[2].x, 1e-10);
        assertEquals(6.0, points[2].y, 1e-10);
        assertEquals(4.0, points[3].x, 1e-10);
        assertEquals(8.0, points[3].y, 1e-10);
    }

    @Test
    void testAsPointsWithSinglePoint() {
        double[] xValues = {5.0};
        double[] yValues = {10.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        assertEquals(1, points.length);
        assertEquals(5.0, points[0].x, 1e-10);
        assertEquals(10.0, points[0].y, 1e-10);
    }

    @Test
    void testAsPointsWithEmptyFunction() {
        double[] xValues = {};
        double[] yValues = {};
        assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
    }

    @Test
    void testAsPointsWithNullFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            TabulatedFunctionOperationService.asPoints(null);
        });
    }

    @Test
    void testAsPointsOrderPreservation() {
        double[] xValues = {3.0, 1.0, 2.0};
        double[] yValues = {30.0, 10.0, 20.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        assertEquals(3.0, points[0].x, 1e-10);
        assertEquals(30.0, points[0].y, 1e-10);
        assertEquals(1.0, points[1].x, 1e-10);
        assertEquals(10.0, points[1].y, 1e-10);
        assertEquals(2.0, points[2].x, 1e-10);
        assertEquals(20.0, points[2].y, 1e-10);
    }
}