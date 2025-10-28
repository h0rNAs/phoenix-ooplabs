package ru.ssau.tk.phoenix.ooplabs.functions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.phoenix.ooplabs.exceptions.DifferentLenghtOfArraysException;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {
    protected Logger logger = LogManager.getLogger(super.getClass());

    protected abstract int floorIndexOfX(double x);
    protected abstract double extrapolateLeft(double x);
    protected abstract double extrapolateRight(double x);
    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    @Override
    public double apply(double x) {
        logger.info(String.format("Вызов метода apply для x = %.2f", x));
        if (x < leftBound()) {
            logger.debug("Применена экстраполяция слева");
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            logger.debug("Применена экстраполяция справа");
            return extrapolateRight(x);
        } else {
            logger.debug("Применена интерполяция");
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }
    public static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLenghtOfArraysException("Arrays must have the same length");
        }
    }

    public static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new ArrayIsNotSortedException("Values must be ordered");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder(getClass().getSimpleName() + " size = " + getCount());
        for (Point point : this){
            strBuilder.append("\n[" + point.x + "; " + point.y + "]");
        }
        return strBuilder.toString();
    }
}
