package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.util.concurrent.RecursiveAction;

public class TrapezoidIntegralOperator implements IntegralOperator{
    private TabulatedFunction func;

    public TrapezoidIntegralOperator(TabulatedFunction func) {
        this.func = func;
    }

    @Override
    public double derive(int startIndex, int endIndex) {
        double h = (func.getX(endIndex) - func.getX(startIndex)) / (endIndex - startIndex);
        double sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += (func.getY(i) + func.getY(i+1)) * h / 2;
        }
        return sum;
    }
}
