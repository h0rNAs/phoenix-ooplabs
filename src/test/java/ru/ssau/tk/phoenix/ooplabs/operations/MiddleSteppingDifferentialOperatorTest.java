package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.MathFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MiddleSteppingDifferentialOperatorTest {
    @Test
    void MiddleStep_DeriveWithSqr1() {
        double step = 0.1;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(1.0);
        assertEquals(2.0, result, 1e-10);
    }
    @Test
    void MiddleStep_DeriveWithSqr2() {
        double step = 0.1;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(2.0);
        assertEquals(4.0, result, 1e-10);
    }
    @Test
    void MiddleStep_DeriveWithLinear() {

        double step = 0.1;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction linearFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 4 * x + 3;
            }
        };
        MathFunction derivative = operator.derive(linearFunction);
        double result = derivative.apply(3.0);
        assertEquals(4.0, result, 1e-10);
    }

    @Test
    void MiddleStep_DeriveWithConstantFunction() {
        double step = 0.1;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction constantFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 10;
            }
        };
        MathFunction derivative = operator.derive(constantFunction);
        double result = derivative.apply(8.0);
        assertEquals(0.0, result, 1e-10);
    }
    @Test
    void MiddleStep_OperatorMoreAccurate() {
        double step = 0.1;
        MathFunction sqrFunction = new SqrFunction();
        LeftSteppingDifferentialOperator leftOp = new LeftSteppingDifferentialOperator(step);
        RightSteppingDifferentialOperator rightOp = new RightSteppingDifferentialOperator(step);
        MiddleSteppingDifferentialOperator middleOp = new MiddleSteppingDifferentialOperator(step);
        double exactDerivative = 2.0;
        double leftResult = leftOp.derive(sqrFunction).apply(1.0);
        double rightResult = rightOp.derive(sqrFunction).apply(1.0);
        double middleResult = middleOp.derive(sqrFunction).apply(1.0);
        double leftError = Math.abs(leftResult - exactDerivative);
        double rightError = Math.abs(rightResult - exactDerivative);
        double middleError = Math.abs(middleResult - exactDerivative);
        assertTrue(middleError < leftError);
        assertTrue(middleError < rightError);
        assertEquals(0.0, middleError, 1e-10);
    }
}