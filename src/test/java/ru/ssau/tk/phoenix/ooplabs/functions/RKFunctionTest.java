package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RKFunctionTest {
    @Test
    void testBoundaryValues() {
        MathFunction derivative = x -> 1.0;
        RKFunction solver = new RKFunction(0.0, 5.0, 0.1, 10, derivative);
        assertEquals(5.0, solver.getValueAt(-1.0), 1e-10);
        assertEquals(5.0, solver.getValueAt(0.0), 1e-10);
        double endValue = solver.getValueAt(1.0);
        assertEquals(endValue, solver.getValueAt(2.0), 1e-10);
    }
    @Test
    void testFewSteps() {
        MathFunction derivative = x -> 1.0;
        RKFunction solver = new RKFunction(0.0, 0.0, 1.0, 1, derivative);

        assertEquals(1.0, solver.getValueAt(1.0), 1e-10);
        assertEquals(1.0, solver.getValueAt(2.0), 1e-10);
    }
    @Test
    void testNegativeIntegrationDirection() {
        MathFunction derivative = x -> 1.0; // y' = 1
        RKFunction solver = new RKFunction(0.0, 0.0, -0.1, 10, derivative);
        double result = solver.getValueAt(-1.0);
        assertEquals(-1.0, result, 1e-4);
    }
    @Test
    void testDiscontinuousDerivative() {
        MathFunction derivative = x -> (x < 0.5) ? 1.0 : 2.0;
        RKFunction solver = new RKFunction(0.0, 0.0, 0.1, 10, derivative);
        double result = solver.getValueAt(1.0);
        assertTrue(result > 1.0 && result < 2.0);
    }
    @Test
    void testVerySmallStep() {
        MathFunction derivative = x -> Math.exp(x); // y' = e^x
        RKFunction solver = new RKFunction(0.0, 1.0, 0.0001, 10000, derivative);
        double result = solver.getValueAt(1.0);
        double expected = Math.exp(1.0); // e^1 ≈ 2.71828
        assertEquals(expected, result, 1e-8); // высокая точность
    }
    @Test
    void testVeryLargeStep() {
        MathFunction derivative = x -> 1.0; // y' = 1
        RKFunction solver = new RKFunction(0.0, 0.0, 2.0, 1, derivative);
        double result = solver.getValueAt(2.0);
        assertTrue(result > 1.5 && result < 2.5); // грубая проверка
    }
}
//