package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqrFunctionTest {
    final SqrFunction SQR_F = new SqrFunction();
    final double DELTA = 1e-15;

    @Test
    void apply_NaN() {
        assertTrue(Double.isNaN(SQR_F.apply(Double.NaN)));
    }

    @Test
    void apply_overflow() {
        assertEquals(Double.POSITIVE_INFINITY, SQR_F.apply(Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, SQR_F.apply(Double.NEGATIVE_INFINITY));
    }

    @Test
    void apply_small() {
        assertEquals(1E-300, SQR_F.apply(1E-150), DELTA);
        assertEquals(0., SQR_F.apply(1E-200 * 1E-200), DELTA);
    }

    @Test
    void apply_big() {
        double actual = 1e300, expected = SQR_F.apply(1e150);
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError <= DELTA,
                "Квадрат большого числа. Ожидалось: " + expected + ", Получено: " + actual +
                        ", Относительная ошибка: " + relativeError);
        assertEquals(Double.POSITIVE_INFINITY, SQR_F.apply(1e300), DELTA);
    }
}
