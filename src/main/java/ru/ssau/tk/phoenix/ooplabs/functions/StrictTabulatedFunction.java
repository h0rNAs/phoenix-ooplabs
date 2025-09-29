package ru.ssau.tk.phoenix.ooplabs.functions;

import java.util.Iterator;

public class StrictTabulatedFunction implements TabulatedFunction{
    private TabulatedFunction tabulatedFunc;


    public StrictTabulatedFunction(TabulatedFunction tabulatedFunc) {
        this.tabulatedFunc = tabulatedFunc;
    }

    @Override
    public int getCount() {
        return tabulatedFunc.getCount();
    }

    @Override
    public double getX(int index) {
        return tabulatedFunc.getX(index);
    }

    @Override
    public double getY(int index) {
        return tabulatedFunc.getY(index);
    }

    @Override
    public void setY(int index, double y) {
        tabulatedFunc.setY(index, y);
    }

    @Override
    public int indexOfX(double x) {
        return tabulatedFunc.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return tabulatedFunc.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return tabulatedFunc.leftBound();
    }

    @Override
    public double rightBound() {
        return tabulatedFunc.rightBound();
    }

    @Override
    public Iterator<Point> iterator() {
        return null;
    }

    @Override
    public double apply(double x) {
        int index = indexOfX(x);
        if (index != -1) return getY(index);
        else throw new UnsupportedOperationException();
    }
}
