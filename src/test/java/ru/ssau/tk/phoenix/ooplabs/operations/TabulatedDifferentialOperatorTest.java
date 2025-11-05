package ru.ssau.tk.phoenix.ooplabs.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.*;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedDifferentialOperatorTest {

    @Test
    void derive_Sqr() {
        TabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), 0, 4, 5);
        TabulatedDifferentialOperator diff = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction diffFunc = diff.derive(func);

        assertEquals(-3, diffFunc.apply(-2));
        assertEquals(1, diffFunc.apply(0));
        assertEquals(7, diffFunc.apply(3));
    }

    @Test
    void derive_Constant(){
        TabulatedFunction func = new ArrayTabulatedFunction(new ConstantFunction(5), 0, 5, 6);
        TabulatedDifferentialOperator diff = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction diffFunc = diff.derive(func);

        assertEquals(0, diffFunc.apply(0));
        assertEquals(0, diffFunc.apply(4));
        assertEquals(0, diffFunc.apply(4435));
    }
    @Test
    public void deriveSynchronously_WithBasicFunction() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        double[] xValues = {1, 2, 3, 4};
        double[] yValues = {1, 4, 9, 16};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction derivative = operator.deriveSynchronously(function);
        assertEquals(derivative.getCount(), 4);
        assertEquals(derivative.getX(0), 1.0, 1e-9);
        assertEquals(derivative.getY(0), 3.0, 1e-9);
        assertEquals(derivative.getX(1), 2.0, 1e-9);
        assertEquals(derivative.getY(1), 5.0, 1e-9);
        assertEquals(derivative.getX(2), 3.0, 1e-9);
        assertEquals(derivative.getY(2), 7.0, 1e-9);
        assertEquals(derivative.getX(3), 4.0, 1e-9);
        assertEquals(derivative.getY(3), 7.0, 1e-9);
    }

    @Test
    public void deriveSynchronously_AlreadySynchronously() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        double[] xValues = {0, 1, 2};
        double[] yValues = {0, 1, 4};
        ArrayTabulatedFunction innerFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(innerFunction);
        TabulatedFunction derivative = operator.deriveSynchronously(syncFunction);
        assertEquals(derivative.getCount(), 3);
        assertEquals(derivative.getX(0), 0.0, 1e-9);
        assertEquals(derivative.getY(0), 1.0, 1e-9);
        assertEquals(derivative.getX(1), 1.0, 1e-9);
        assertEquals(derivative.getY(1), 3.0, 1e-9);
        assertEquals(derivative.getX(2), 2.0, 1e-9);
        assertEquals(derivative.getY(2), 3.0, 1e-9);
    }
}