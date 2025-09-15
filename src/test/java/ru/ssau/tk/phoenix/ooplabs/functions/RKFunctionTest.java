package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RKFunctionTest {
    @Test
    public void testNegativeStep() {
        RKFunction rk = new RKFunction(0, 1, -0.1);
        assertThrows(IllegalArgumentException.class, () -> rk.apply(1));
        assertThrows(IllegalArgumentException.class, () -> rk.apply(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testTargetEqualToInitial() {
        RKFunction rk = new RKFunction(0, 1, 0.1);
        assertEquals(1, rk.apply(0), 1e-10);
    }
}
