package ru.ssau.tk.phoenix.ooplabs.functions;
        import org.junit.jupiter.api.Test;
        import static org.junit.jupiter.api.Assertions.*;
class UnitFunctionTest {
    private static final double DELTA = 1e-15;
    @Test
    public void testConstantFunction() {
        UnitFunction unitFunc = new UnitFunction(1.0);
        assertEquals(1.0, unitFunc.apply(0), DELTA);
        assertEquals(1.0, unitFunc.apply(-50), DELTA);
    }

}
