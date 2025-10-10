package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private final TabulatedFunction func;
    private final Object lock;

    public SynchronizedTabulatedFunction(TabulatedFunction func, Object lock) {
        this.func = func;
        this.lock = lock;
    }

    @Override
    public int getCount() {
        synchronized (lock) {return func.getCount();}
    }

    @Override
    public double getX(int index) {
        synchronized (lock) {return func.getX(index);}
    }

    @Override
    public double getY(int index) {
        synchronized (lock) {return func.getY(index);}
    }

    @Override
    public void setY(int index, double value) {
        synchronized (lock) {func.setY(index, value);}
    }

    @Override
    public int indexOfX(double x) {
        synchronized (lock) {return func.indexOfX(x);}
    }

    @Override
    public int indexOfY(double y) {
        synchronized (lock) {return func.indexOfY(y);}
    }

    @Override
    public double leftBound() {
        synchronized (lock) {return func.leftBound();}
    }

    @Override
    public double rightBound() {
        synchronized (lock) {return func.rightBound();}
    }

    @Override
    public Iterator<Point> iterator() {
        Point[] points;
        synchronized (lock){
            points = TabulatedFunctionOperationService.asPoints(func);
        }

        return new Iterator<Point>() {
            private int index;
            @Override
            public boolean hasNext() {
                return index < points.length;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the tabulated function");
                }
                return points[index++];
            }
        };
    }

    @Override
    public double apply(double x) {
        synchronized (lock) {return func.apply(x);}
    }
}
