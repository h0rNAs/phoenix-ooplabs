package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.MathFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RightSteppingDifferentialOperatorTest {
    @Test
    void RightStep_DeriveWithSqr1() {
        double step = 0.1;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(1.0);
        assertEquals(2.1, result, 1e-10);
    }
    @Test
    void RightStep_DeriveWithSqr2() {
        double step = 0.1;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(2.0);
        assertEquals(4.1, result, 1e-10);
    }
    @Test
    void RightStep_DeriveWithLinear() {
        double step = 0.1;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction linearFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 3 * x - 2;
            }
        };
        MathFunction derivative = operator.derive(linearFunction);
        double result = derivative.apply(4.0);
        assertEquals(3.0, result, 1e-10);
    }
    @Test
    void RightStep_DeriveWithConstant() {
        double step = 0.1;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction constantFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 7;
            }
        };
        MathFunction derivative = operator.derive(constantFunction);
        double result = derivative.apply(5.0);
        assertEquals(0.0, result, 1e-10);
    }
    @Test
    void RightStep_SmallStep() {
        double smallStep = 0.001;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(smallStep);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(1.0);
        assertEquals(2.001, result, 1e-10);
    }
}