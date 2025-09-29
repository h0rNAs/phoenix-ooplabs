package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.MathFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LeftSteppingDifferentialOperatorTest {
    @Test
    void LeftStep_DeriveWithSqr1() {
        double step = 0.1;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(1.0);
        assertEquals(1.9, result, 1e-10);
    }
    @Test
    void LeftStep_DeriveWithSqr2() {
        double step = 0.1;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(2.0);
        assertEquals(3.9, result, 1e-10);
    }
    @Test
    void LeftStep_DeriveWithLinear() {
        double step = 0.1;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);

        MathFunction linearFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 2 * x + 1;
            }
        };
        MathFunction derivative = operator.derive(linearFunction);
        double result = derivative.apply(3.0);
        assertEquals(2.0, result, 1e-10);
    }
    @Test
    void LeftStep_DeriveWithConstant() {
        double step = 0.1;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction constantFunction = new MathFunction() {
            @Override
            public double apply(double x) {
                return 5;
            }
        };
        MathFunction derivative = operator.derive(constantFunction);
        double result = derivative.apply(10.0);
        assertEquals(0.0, result, 1e-10);
    }
    @Test
    void LeftStep_SmallStep() {
        double smallStep = 0.001;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(smallStep);
        MathFunction sqrFunction = new SqrFunction();
        MathFunction derivative = operator.derive(sqrFunction);
        double result = derivative.apply(1.0);
        assertEquals(1.999, result, 1e-10);
    }
}