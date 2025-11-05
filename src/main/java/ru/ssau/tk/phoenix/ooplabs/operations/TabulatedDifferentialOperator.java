package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {
    private TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator() {
        factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        int count = points.length;
        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count-1; i++){
            yValues[i] = (points[i+1].y - points[i].y) / (points[i+1].x - points[i].x);
            xValues[i] = points[i].x;
        }
        xValues[count-1] = points[count-1].x;
        yValues[count-1] = (points[count-1].y - points[count-2].y) / (points[count-1].x - points[count-2].x);

        return factory.create(xValues, yValues);
    }


    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
    public TabulatedFunction deriveSynchronously(TabulatedFunction function){
        SynchronizedTabulatedFunction syncFunction;
        if (function instanceof SynchronizedTabulatedFunction) {
            syncFunction = (SynchronizedTabulatedFunction) function;
        } else {
            syncFunction = new SynchronizedTabulatedFunction(function);
        }
        return syncFunction.doSynchronously((SynchronizedTabulatedFunction f) -> this.derive(f));
    }
}
