package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {

    @Test
    void constructor_differentLengthArrays() {
        double[] xValues = new double[]{0, 1, 2, 3};
        double[] yValues = new double[]{5, 4, 2};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }

    @Test
    void constructor_anSortedArray(){
        double[] xValues = new double[]{0, 2, 1};
        double[] yValues = new double[]{5, 4, 2};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }

    @Test
    void setY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xValues, yValues);

        f.setY(1, 1);
        assertEquals(1, f.getY(1));
        assertThrows(IndexOutOfBoundsException.class, () -> f.setY(-2, 1));
    }

    @Test
    void indexOfX() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(2, func.indexOfX(2));
        assertEquals(-1, func.indexOfX(234));
    }

    @Test
    void indexOfY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(1, func.indexOfY(4));
        assertEquals(-1, func.indexOfY(234));
    }

    @Test
    void leftAndRightBound() {
        double[] xValues = new double[]{2, 16, 24};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(2, func.leftBound());
        assertEquals(24, func.rightBound());
    }


    @Test
    void apply() {
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), 0, 4, 5);
        assertEquals(4, func.apply(2));
        assertEquals(23, func.apply(5));
        assertEquals(2.5, func.apply(1.5));
        assertEquals(-1, func.apply(-1));
    }
}