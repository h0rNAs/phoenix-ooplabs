package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathFunctionTest {
    static final double DELTA = 1e-15;

    @Test
    void testAndThenReturnsCompositeFunction() {
        MathFunction constant = new ConstantFunction(2.0);
        MathFunction square = x -> x * x;
        MathFunction result = constant.andThen(square);
        assertTrue(result instanceof CompositeFunction);
    }
    @Test
    void testAndThenWithConstantFunctions() {
        MathFunction zero = new ZeroFunction();
        MathFunction unit = new UnitFunction();
        MathFunction constant = new ConstantFunction(5.0);
        MathFunction zeroThenUnit = zero.andThen(unit);
        assertEquals(zeroThenUnit.apply(10), 1.0, DELTA);
        MathFunction unitThenConstant = unit.andThen(constant);
        assertEquals(unitThenConstant.apply(10), 5.0, DELTA);
    }
    @Test
    void testDeBoorFunctionAndThen() {
        double[] knots = {0, 0, 1, 2, 2};
        double[] controlPoints = {1.0, 3.0, 2.0};
        int degree = 1;
        DeBoorFunction deBoorFunction = new DeBoorFunction(knots, controlPoints, degree);
        SqrFunction sqrFunction = new SqrFunction();
        MathFunction deBoorThenDouble = deBoorFunction.andThen(sqrFunction);
        assertEquals(deBoorThenDouble.apply(1.5), 6.25, 0.1);
        assertEquals(deBoorThenDouble.apply(0.5), 4.0, 0.1);
    }

    @Test
    void testRKFunctionAndThen(){
        RKFunction rkFunction = new RKFunction(0, 1, 0.1);
        SqrFunction sqrFunction = new SqrFunction();
        MathFunction rkThenDouble = rkFunction.andThen(sqrFunction);
        assertEquals(rkThenDouble.apply(0), 1) ;

    }
}
