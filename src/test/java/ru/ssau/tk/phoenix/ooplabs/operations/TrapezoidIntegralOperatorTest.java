package ru.ssau.tk.phoenix.ooplabs.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.concurrent.IntegralTask;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class TrapezoidIntegralOperatorTest {

    @Test
    void derive() {
        TabulatedFunction func = new ArrayTabulatedFunction(new SqrFunction(), -1000, 300, 1301);
        IntegralOperator op = new TrapezoidIntegralOperator(func);
        ForkJoinPool pool = new ForkJoinPool();
        double res = pool.invoke(new IntegralTask(0, func.getCount()-1, op));
        assertEquals(3.42334E8, res, 1000);
    }
}