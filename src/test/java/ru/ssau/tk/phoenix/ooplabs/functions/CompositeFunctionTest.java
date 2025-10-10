package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionTest {
    final double DELTA = 1e-15;

    @Test
    void apply_sqrToSqr(){
        CompositeFunction comp = new CompositeFunction(new SqrFunction(), new SqrFunction());

        assertTrue(Double.isNaN(comp.apply(Double.NaN)));
        assertEquals(Double.POSITIVE_INFINITY, comp.apply(Double.POSITIVE_INFINITY));
        assertEquals(1E-300, comp.apply(1E-75), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, comp.apply(1e100), DELTA);

        double actual = 1e300, expected = comp.apply(1e75);
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError <= DELTA,
                "Квадрат большого числа. Ожидалось: " + expected + ", Получено: " + actual +
                        ", Относительная ошибка: " + relativeError);
    }

    @Test
    void apply_sqrToDeBoor(){
        double[] knots = {0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 5, 5, 5, 5};
        double[] controlPoints = {0.0, 1.0, 3.0, 2.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 3);
        CompositeFunction comp = new CompositeFunction(new SqrFunction(), deBoor);

        assertEquals(0.0, deBoor.apply(0.0));
        assertEquals(9.0, deBoor.apply(9.0));
        assertEquals(5.0, deBoor.apply(3.0));
    }

    @Test
    void apply_sqrToSK(){
        RKFunction rk = new RKFunction(0.0, 5.0, 0.5);
        CompositeFunction comp = new CompositeFunction(new SqrFunction(), rk);
        assertEquals(13, comp.apply(2));
    }
}