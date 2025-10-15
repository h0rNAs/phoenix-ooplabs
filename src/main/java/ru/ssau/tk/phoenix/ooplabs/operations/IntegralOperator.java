package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.MathFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public interface IntegralOperator{
    double derive(int startIndex, int endIndex);
}
