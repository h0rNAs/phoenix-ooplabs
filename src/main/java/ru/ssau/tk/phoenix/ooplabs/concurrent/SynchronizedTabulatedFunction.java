package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private final TabulatedFunction func;

    public SynchronizedTabulatedFunction(TabulatedFunction func) {
        this.func = func;
    }

    @Override
    public int getCount() {
        synchronized (func) {return func.getCount();}
    }

    @Override
    public double getX(int index) {
        synchronized (func) {return func.getX(index);}
    }

    @Override
    public double getY(int index) {
        synchronized (func) {return func.getY(index);}
    }

    @Override
    public void setY(int index, double value) {
        synchronized (func) {func.setY(index, value);}
    }

    @Override
    public int indexOfX(double x) {
        synchronized (func) {return func.indexOfX(x);}
    }

    @Override
    public int indexOfY(double y) {
        synchronized (func) {return func.indexOfY(y);}
    }

    @Override
    public double leftBound() {
        synchronized (func) {return func.leftBound();}
    }

    @Override
    public double rightBound() {
        synchronized (func) {return func.rightBound();}
    }

    @Override
    public Iterator<Point> iterator() {
        Point[] points;
        synchronized (func){
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
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }
    public <T> T doSynchronously(Operation<? extends T> operation) {
        synchronized (func) {
            return operation.apply(this);
        }
    }

    @Override
    public double apply(double x) {
        synchronized (func) {return func.apply(x);}
    }
}
