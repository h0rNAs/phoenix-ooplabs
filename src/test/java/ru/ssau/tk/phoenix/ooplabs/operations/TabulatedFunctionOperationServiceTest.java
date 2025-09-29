package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.phoenix.ooplabs.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;

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
        assertThrows(IllegalArgumentException.class, () -> {
            TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        });
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
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
        assertThrows(ArrayIsNotSortedException.class, () -> {
            TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        });
        Exception exception = assertThrows(ArrayIsNotSortedException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
    }
    @Test
    void testAddWithSameTypeFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);
        TabulatedFunction result = service.add(func1, func2);
        assertEquals(3, result.getCount());
        assertEquals(3.0, result.getY(0), 1e-10);
        assertEquals(6.0, result.getY(1), 1e-10);
        assertEquals(9.0, result.getY(2), 1e-10);
    }

    @Test
    void testAddWithDifferentTypeFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);
        TabulatedFunction result = service.add(func1, func2);
        assertEquals(3, result.getCount());
        assertEquals(3.0, result.getY(0), 1e-10);
        assertEquals(6.0, result.getY(1), 1e-10);
        assertEquals(9.0, result.getY(2), 1e-10);
    }
    @Test
    void testSubtractWithDifferentTypeFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {5.0, 8.0, 11.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);

        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {2.0, 3.0, 4.0};
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.subtract(func1, func2);

        assertEquals(3, result.getCount());
        assertEquals(3.0, result.getY(0), 1e-10);
        assertEquals(5.0, result.getY(1), 1e-10);
        assertEquals(7.0, result.getY(2), 1e-10);
    }

    @Test
    void multiply_ArrayAndArray(){
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        TabulatedFunction resFunc = service.multiply(func1, func2);

        assertEquals(2.0, resFunc.apply(1));
        assertEquals(8.0, resFunc.apply(2));
        assertEquals(18.0, resFunc.apply(3));
    }
    @Test
    void multiply_ArrayAndList(){
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction resFunc = service.multiply(func1, func2);

        assertEquals(2.0, resFunc.apply(1));
        assertEquals(8.0, resFunc.apply(2));
        assertEquals(18.0, resFunc.apply(3));
    }

    @Test
    void divide_ArrayAndArray(){
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        TabulatedFunction resFunc = service.divide(func1, func2);

        assertEquals(2.0, resFunc.apply(1));
        assertEquals(2.0, resFunc.apply(2));
        assertEquals(2.0, resFunc.apply(3));
    }

    @Test
    void divide_ArrayAndList(){
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction resFunc = service.divide(func1, func2);

        assertEquals(2.0, resFunc.apply(1));
        assertEquals(2.0, resFunc.apply(2));
        assertEquals(2.0, resFunc.apply(3));
    }

    @Test
    void doOperation_DifferentXArray(){
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 5.0, 42343.0};
        double[] yValues2 = {1.0, 2.0, 3.0};
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        assertThrows(InconsistentFunctionsException.class, () ->
                new TabulatedFunctionOperationService().add(func1, func2));
    }

    @Test
    void doOperation_DifferentArrayLength(){
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {1.0, 2.0, 3.0, 4.0};
        double[] yValues2 = {1.0, 2.0, 3.0, 4.0};
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        assertThrows(InconsistentFunctionsException.class, () ->
                new TabulatedFunctionOperationService().add(func1, func2));
    }
}