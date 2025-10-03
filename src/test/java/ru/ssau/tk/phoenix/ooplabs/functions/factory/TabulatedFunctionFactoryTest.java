package ru.ssau.tk.phoenix.ooplabs.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.functions.*;
import ru.ssau.tk.phoenix.ooplabs.functions.UnmodifiableTabulatedFunction;

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

    @Test
    void createStrict_Array(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        TabulatedFunction func = new ArrayTabulatedFunctionFactory().createStrict(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, func);

        StrictTabulatedFunction strict = (StrictTabulatedFunction) func;
        assertEquals(4, strict.apply(1));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }

    @Test
    void createStrict_LinkedList(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        TabulatedFunction func = new LinkedListTabulatedFunctionFactory().createStrict(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, func);

        StrictTabulatedFunction strict = (StrictTabulatedFunction) func;
        assertEquals(4, strict.apply(1));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }

    @Test
    void createStrictUnmodifiable() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        TabulatedFunction func = new ArrayTabulatedFunctionFactory().createStrictUnmodifiable(xValues, yValues);
        assertThrows(UnsupportedOperationException.class, () -> func.setY(1, 234));
        assertThrows(UnsupportedOperationException.class, () -> func.apply(1.5));
        assertEquals(4, func.apply(1));
    }
    @Test
    void createUnmodifiable_Array() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction unmodifiableFunction = factory.createUnmodifiable(xValues, yValues);
        assertNotNull(unmodifiableFunction);
        assertEquals(3, unmodifiableFunction.getCount());
        assertTrue(unmodifiableFunction instanceof UnmodifiableTabulatedFunction);
        assertEquals(1.0, unmodifiableFunction.getX(0), 1e-10);
        assertEquals(4.0, unmodifiableFunction.getY(0), 1e-10);
        assertEquals(2.0, unmodifiableFunction.getX(1), 1e-10);
        assertEquals(5.0, unmodifiableFunction.getY(1), 1e-10);
        assertEquals(3.0, unmodifiableFunction.getX(2), 1e-10);
        assertEquals(6.0, unmodifiableFunction.getY(2), 1e-10);
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiableFunction.setY(1, 10.0)
        );
    }

    @Test
    void createUnmodifiable_Linked() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction unmodifiableFunction = factory.createUnmodifiable(xValues, yValues);
        assertNotNull(unmodifiableFunction);
        assertEquals(3, unmodifiableFunction.getCount());
        assertTrue(unmodifiableFunction instanceof UnmodifiableTabulatedFunction);
        assertEquals(1.0, unmodifiableFunction.getX(0), 1e-10);
        assertEquals(4.0, unmodifiableFunction.getY(0), 1e-10);
        assertEquals(2.0, unmodifiableFunction.getX(1), 1e-10);
        assertEquals(5.0, unmodifiableFunction.getY(1), 1e-10);
        assertEquals(3.0, unmodifiableFunction.getX(2), 1e-10);
        assertEquals(6.0, unmodifiableFunction.getY(2), 1e-10);
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiableFunction.setY(1, 10.0)
        );
    }
}