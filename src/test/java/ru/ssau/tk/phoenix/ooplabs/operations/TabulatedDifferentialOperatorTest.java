package ru.ssau.tk.phoenix.ooplabs.operations;

import org.junit.jupiter.api.Test;
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
}