package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.exceptions.InconsistentFunctionsException;

import java.util.ArrayList;
import java.util.List;

public class TabulatedFunctionOperationService {
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        if (tabulatedFunction == null) {
            throw new IllegalArgumentException("Tabulated function cannot be null");
        }
        List<Point> pointList = new ArrayList<>();
        for (Point point : tabulatedFunction) {
            pointList.add(point);
        }
        return pointList.toArray(new Point[0]);
    }

    private interface BinOperation {
        double apply(double u, double v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BinOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException("Functions have different number of points");
        }
        Point[] pointsA = asPoints(a);
        Point[] pointsB = asPoints(b);
        int count = pointsA.length;
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        for (int i = 0; i < count; i++) {
            if (Math.abs(pointsA[i].x - pointsB[i].x) > 1e-9) {
                throw new InconsistentFunctionsException("X values don't match at index " + i);
            }
            xValues[i] = pointsA[i].x;
            yValues[i] = operation.apply(pointsA[i].y, pointsB[i].y);
        }
        return factory.create(xValues, yValues);
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, new BinOperation() {
            @Override
            public double apply(double u, double v) {
                return u + v;
            }
        });
    }

    public TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, new BinOperation() {
            @Override
            public double apply(double u, double v) {
                return u - v;
            }
        });
    }
}

