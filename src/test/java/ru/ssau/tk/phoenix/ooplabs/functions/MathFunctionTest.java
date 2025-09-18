package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathFunctionTest {
    private static final double DELTA = 1e-15;
    @Test
    public void testAndThenReturnsCompositeFunction() {
        MathFunction constant = new ConstantFunction(2.0);
        MathFunction square = x -> x * x;
        MathFunction result = constant.andThen(square);
        assertTrue(result instanceof CompositeFunction);
    }
    @Test
    public void testAndThenWithConstantFunctions() {
        MathFunction zero = new ZeroFunction(0.0);
        MathFunction unit = new UnitFunction(1.0);
        MathFunction constant = new ConstantFunction(5.0);
        MathFunction zeroThenUnit = zero.andThen(unit);
        assertEquals(zeroThenUnit.apply(10), 1.0, DELTA);
        MathFunction unitThenConstant = unit.andThen(constant);
        assertEquals(unitThenConstant.apply(10), 5.0, DELTA);
    }
    @Test
    public void testCompositeFunctionStructure() {
        MathFunction constant = new ConstantFunction(3.0);
        MathFunction square = x -> x * x;
        CompositeFunction composite = (CompositeFunction) constant.andThen(square);
        assertEquals(composite.f.apply(5), 3.0, DELTA);
        assertEquals(composite.g.apply(3), 9.0, DELTA);
    }
}