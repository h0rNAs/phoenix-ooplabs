package ru.ssau.tk.phoenix.ooplabs.functions.factory;

import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);
}
