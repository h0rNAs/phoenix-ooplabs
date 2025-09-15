package ru.ssau.tk.phoenix.ooplabs.functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ConstantFunctionTest {
        private static final double DELTA = 1e-15;
        @Test
        public void testConstantFunction() {
            ConstantFunction constFunc = new ConstantFunction(5.0);
            assertEquals(5.0, constFunc.apply(10), DELTA);
            assertEquals(5.0, constFunc.apply(-10), DELTA);
        }

}
