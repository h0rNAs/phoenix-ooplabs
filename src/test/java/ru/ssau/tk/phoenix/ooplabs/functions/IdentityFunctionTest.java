package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IdentityFunctionTest {
    IdentityFunction f = new IdentityFunction();

    @Test
    void apply_overflow() {
        assertEquals(Double.MAX_VALUE*1.1, f.apply(Double.MAX_VALUE*1.1));
    }

    @Test
    void apply_NaN() {
        assertEquals(Double.NaN, f.apply(Double.NaN));
    }
}