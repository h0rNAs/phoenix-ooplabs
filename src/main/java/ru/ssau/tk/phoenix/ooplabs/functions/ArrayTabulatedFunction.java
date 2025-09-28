package ru.ssau.tk.phoenix.ooplabs.functions;
import ru.ssau.tk.phoenix.ooplabs.exceptions.InterpolationException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    private double[] xValues;
    private double[] yValues;
    private int count;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);  // ← НОВОЕ
        if (xValues.length < 2) {
            throw new IllegalArgumentException("At least 2 point is required");
        }
        checkSorted(xValues);
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("At least 2 point is required");
        }
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }
        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];
        if (xFrom == xTo) {
            double value = source.apply(xFrom);
            Arrays.fill(xValues, xFrom);
            Arrays.fill(yValues, value);
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                double x = xFrom + i * step;
                xValues[i] = x;
                yValues[i] = source.apply(x);
            }
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(xValues[i] - x) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(yValues[i] - y) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) throw new IllegalArgumentException("x out of left bound");
        if (x > xValues[count - 1]) return count;
        for (int i = 1; i < count; i++) {
            if (x < xValues[i]) {
                return i - 1;
            }
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count-2], xValues[count-1], yValues[count-2], yValues[count-1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (floorIndex < 0 || floorIndex >= count - 1) {
            throw new IllegalArgumentException("Invalid floor index");
        }
        double leftX = xValues[floorIndex];
        double rightX = xValues[floorIndex + 1];
        if (x < leftX || x > rightX) {
            throw new InterpolationException("X value is outside the interpolation interval");
        }
        return interpolate(x, leftX, rightX, yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public void insert(double x, double y) {
        int index = indexOfX(x);
        if (index != -1) setY(index, y);
        else {
            if (x < xValues[0]) index = 1;
            else index = floorIndexOfX(x) + 1;

            double[] newXArray = new double[count <= xValues.length ? count + 1 : count];
            double[] newYArray = new double[count <= xValues.length ? count + 1 : count];

            System.arraycopy(xValues, 0, newXArray, 0, index);
            System.arraycopy(yValues, 0, newYArray, 0, index);
            newXArray[index] = x;
            newYArray[index] = y;
            System.arraycopy(xValues, index, newXArray, index + 1, count - index);
            System.arraycopy(yValues, index, newYArray, index + 1, count - index);

            xValues = newXArray;
            yValues = newYArray;
        }
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }
        if (count == 1) {
            throw new IllegalStateException("Cannot remove the last point from the function");
        }
        double[] newXArray = new double[count - 1];
        double[] newYArray = new double[count - 1];
        System.arraycopy(xValues, 0, newXArray, 0, index);
        System.arraycopy(yValues, 0, newYArray, 0, index);
        System.arraycopy(xValues, index + 1, newXArray, index, count - index - 1);
        System.arraycopy(yValues, index + 1, newYArray, index, count - index - 1);
        xValues = newXArray;
        yValues = newYArray;
        count--;
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < count;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the tabulated function");
                }
                Point point = new Point(xValues[i], yValues[i]);
                i++;
                return point;
            }
        };
    }
}