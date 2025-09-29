package ru.ssau.tk.phoenix.ooplabs.functions.factory;

import ru.ssau.tk.phoenix.ooplabs.functions.StrictTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);
    default TabulatedFunction createStrict(double[] xValues, double[] yValues){
        return new StrictTabulatedFunction(create(xValues, yValues));
    }
}
