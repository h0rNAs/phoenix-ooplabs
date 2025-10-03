package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnmodifiableTabulatedFunctionTest {
    @Test
    void Unmodifiable_Constructor() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertNotNull(unmodifiable);
    }
    @Test
    void Unmodifiable_GetCount() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(3, unmodifiable.getCount());
    }
    @Test
    void Unmodifiable_GetX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(1.0, unmodifiable.getX(0), 1e-10);
        assertEquals(2.0, unmodifiable.getX(1), 1e-10);
        assertEquals(3.0, unmodifiable.getX(2), 1e-10);
    }
    @Test
    void Unmodifiable_GetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(4.0, unmodifiable.getY(0), 1e-10);
        assertEquals(5.0, unmodifiable.getY(1), 1e-10);
        assertEquals(6.0, unmodifiable.getY(2), 1e-10);
    }

    @Test
    void Unmodifiable_SetYThrowsException() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiable.setY(0, 10.0)
        );
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiable.setY(1, 20.0)
        );
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiable.setY(2, 30.0)
        );
    }
    @Test
    void Unmodifiable_IndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(0, unmodifiable.indexOfX(1.0));
        assertEquals(1, unmodifiable.indexOfX(2.0));
        assertEquals(2, unmodifiable.indexOfX(3.0));
        assertEquals(-1, unmodifiable.indexOfX(0.0));
    }
    @Test
    void Unmodifiable_IndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(0, unmodifiable.indexOfY(4.0));
        assertEquals(1, unmodifiable.indexOfY(5.0));
        assertEquals(2, unmodifiable.indexOfY(6.0));
        assertEquals(-1, unmodifiable.indexOfY(0.0));
    }
    @Test
    void Unmodifiable_LeftBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);

        assertEquals(1.0, unmodifiable.leftBound(), 1e-10);
    }

    @Test
    void Unmodifiable_RightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(3.0, unmodifiable.rightBound(), 1e-10);
    }
    @Test
    void Unmodifiable_Apply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(4.0, unmodifiable.apply(1.0), 1e-10);
        assertEquals(5.0, unmodifiable.apply(2.0), 1e-10);
        assertEquals(6.0, unmodifiable.apply(3.0), 1e-10);
        assertEquals(3.0, unmodifiable.apply(0.0), 1e-10);
        assertEquals(7.0, unmodifiable.apply(4.0), 1e-10);
        assertEquals(4.5, unmodifiable.apply(1.5), 1e-10);
    }
    @Test
    void Unmodifiable_Iterator() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        int index = 0;
        for (Point point : unmodifiable) {
            assertEquals(xValues[index], point.x, 1e-10);
            assertEquals(yValues[index], point.y, 1e-10);
            index++;
        }
        assertEquals(3, index);
    }
    @Test
    void Unmodifiable_WithLinkedListTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        TabulatedFunction original = new LinkedListTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);
        assertEquals(3, unmodifiable.getCount());
        assertEquals(1.0, unmodifiable.getX(0), 1e-10);
        assertEquals(4.0, unmodifiable.getY(0), 1e-10);
        assertEquals(1.0, unmodifiable.leftBound(), 1e-10);
        assertEquals(3.0, unmodifiable.rightBound(), 1e-10);
        assertThrows(UnsupportedOperationException.class, () ->
                unmodifiable.setY(0, 10.0)
        );
    }

    @Test
    void unmodifiableToStrict(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        TabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction unmod = new UnmodifiableTabulatedFunction(func);
        TabulatedFunction unmodStrict = new StrictTabulatedFunction(unmod);

        assertEquals(4, unmodStrict.apply(1));
        assertThrows(UnsupportedOperationException.class, () -> unmodStrict.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> unmodStrict.apply(-12));
        assertThrows(UnsupportedOperationException.class, () -> unmodStrict.setY(1, 234));
    }
}