package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IdentityFunctionTest {

    @Test
    void apply() {
        List<Double> actual = Arrays.asList(0., .1, Double.MAX_VALUE+1.);

        IdentityFunction f = new IdentityFunction();
        List<Double> expected = new ArrayList<>();
        expected.add(f.apply(0.));
        expected.add(f.apply(.1));
        expected.add(f.apply(Double.MAX_VALUE+1.));

        assertEquals(expected, actual);
    }
}