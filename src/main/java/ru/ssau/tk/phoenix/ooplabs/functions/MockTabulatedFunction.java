package ru.ssau.tk.phoenix.ooplabs.functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction{
    double[] xValues;
    double[] yValues;


    public MockTabulatedFunction(double[] xValues, double[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public MockTabulatedFunction() {
    }

    @Override
    public int getCount() {
        return xValues.length;
    }

    @Override
    protected int floorIndexOfX(double x) {
        int index = 0;
        for (int i = 0; i < getCount(); i++){
            if (xValues[index] > xValues[i]){
                index = i;
            }
        }
        return index;
    }

    @Override
    protected double extrapolateLeft(double x) {
        double k = (yValues[1] - yValues[0]) / (xValues[1] - xValues[0]);
        double b = yValues[1] - k * xValues[1];

        return k * x + b;
    }

    @Override
    protected double extrapolateRight(double x) {
        double x1 = xValues[getCount() - 2];
        double x2 = xValues[getCount() - 1];
        double y1 = yValues[getCount() - 2];
        double y2 = yValues[getCount() - 1];

        double k = (y2 - y1) / (x2 - x1);
        double b = y2 - k * x2;

        return k * x + b;
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        double x0 = xValues[floorIndex];
        double x1 = xValues[floorIndex + 1];
        double y0 = yValues[floorIndex];
        double y1 = yValues[floorIndex + 1];

        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }

    @Override
    public double getX(int index) {
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < getCount(); i++){
            if (x == xValues[i]) return i;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i <= getCount(); i++){
            if (y == yValues[i]) return i;
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[getCount() - 1];
    }
}
