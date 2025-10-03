package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrictTabulatedFunctionTest {

    @Test
    void apply_LinkedList(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strict = new StrictTabulatedFunction(func);
        assertEquals(4, strict.apply(1));
        assertEquals(2, strict.apply(2));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(3));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }

    @Test
    void apply_Array(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strict = new StrictTabulatedFunction(func);
        assertEquals(2, strict.apply(2));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(3));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
    }

    @Test
    void strictToUnmodifiable(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        TabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction strict = new StrictTabulatedFunction(func);
        TabulatedFunction strictUnmod = new UnmodifiableTabulatedFunction(strict);

        assertEquals(4, strictUnmod.apply(1));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmod.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmod.apply(-12));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmod.setY(1, 234));
    }
}