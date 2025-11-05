package ru.ssau.tk.phoenix.ooplabs.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedTabulatedFunctionTest {
    @Test
    void setY(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction syncFunc = new SynchronizedTabulatedFunction(func);

        syncFunc.setY(2, 50);
        assertEquals(50, syncFunc.apply(3));
    }

    @Test
    void iterator(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction syncFunc = new SynchronizedTabulatedFunction(func);

        var iterator = syncFunc.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().x, 1.0);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().y, 20.0);
    }

    @Test
    void otherMethods(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction syncFunc = new SynchronizedTabulatedFunction(func);

        assertEquals(syncFunc.getCount(), 3);
        assertEquals(syncFunc.getX(1), 2.0);
        assertEquals(syncFunc.getY(2), 30.0);
        assertEquals(syncFunc.indexOfX(3.0), 2);
        assertEquals(syncFunc.indexOfY(20.0), 1);
        assertEquals(syncFunc.leftBound(), 1.0);
        assertEquals(syncFunc.rightBound(), 3.0);
        assertEquals(syncFunc.apply(2.0), 20.0);
        assertEquals(syncFunc.apply(4.0), 40.0);
    }
}