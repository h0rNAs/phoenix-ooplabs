package ru.ssau.tk.phoenix.ooplabs.functions;
        import org.junit.jupiter.api.Test;
        import static org.junit.jupiter.api.Assertions.*;
class ZeroFunctionTest {
    private static final double DELTA = 1e-15;
    @Test
    public void testConstantFunction() {
        ZeroFunction zeroFunc = new ZeroFunction(0.0);
        assertEquals(0.0, zeroFunc.apply(1), DELTA);
        assertEquals(0.0, zeroFunc.apply(100), DELTA);

    }

}
