package ru.ssau.tk.phoenix.ooplabs.functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ArrayTabulatedFunctionTest {
    @Test
    public void testConstructorWithMathFunction() {
        MathFunction source = new SqrFunction();
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(source, 0.0, 4.0, 5);
        assertEquals(function.getCount(), 5);
        assertEquals(0.0, function.getY(0));
        assertEquals(0.0, function.getY(0));
        assertEquals(1.0, function.getY(1));
        assertEquals(1.0, function.getY(1));
        assertEquals(2.0, function.getX(2));
        assertEquals(4.0, function.getY(2));
        assertEquals(3.0, function.getX(3));
        assertEquals(9.0, function.getY(3));
        assertEquals(4.0, function.getX(4));
        assertEquals(16.0, function.getY(4));
    }

    @Test
    public void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(10.0, function.apply(1.0));
        assertEquals(20.0, function.apply(2.0));
        assertEquals(15.0, function.apply(1.5));
        assertEquals(30.0, function.apply(3.0));//Интерполяция
        assertEquals(25.0, function.apply(2.5));//Экстраполяция слева
        assertEquals(5.0, function.apply(0.5));//Экстраполяция справа
        assertEquals(35.0, function.apply(3.5));
    }
    @Test
    public void testApplyWithOnePoint() {
        double[] xValues = {5.0};
        double[] yValues = {10.0};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }
    @Test
    public void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(0, function.indexOfX(1.0));
        assertEquals(1, function.indexOfX(2.0));
        assertEquals(2, function.indexOfX(3.0));
        assertEquals(-1, function.indexOfX(4.0));
    }
    @Test
    public void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(0, function.indexOfY(10.0));
        assertEquals(1, function.indexOfY(20.0));
        assertEquals(2, function.indexOfY(30.0));
        assertEquals(-1, function.indexOfY(40.0));
    }
    @Test
    public void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.setY(1, 25.0);
        assertEquals(25.0, function.getY(1));
        assertThrows(IllegalArgumentException.class, () -> function.setY(-2, 1));
    }
    @Test
    void testGetXAndY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> func.getY(-2));
        assertThrows(IllegalArgumentException.class, () -> func.getX(3424));
    }
    @Test
    void testFloorIndexOfX(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1, func.floorIndexOfX(1.5));
        assertEquals(1, func.floorIndexOfX(1));
        assertThrows(IllegalArgumentException.class, () -> func.floorIndexOfX(-868));
    }
    @Test
    public void testLeftAndRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(1.0, function.leftBound());
        assertEquals(3.0, function.rightBound());
    }
    @Test
    public void testInvalidArrays() {// Не упорядочены
        double[] xValues = {1.0, 3.0, 2.0};
        double[] yValues = {10.0, 20.0, 30.0};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
        assertEquals("Values must be ordered", exception.getMessage());
    }
    @Test
    public void testDifferentLengthArrays() {//Разное Кол-во элементов
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
        assertEquals("Arrays must have the same length", exception.getMessage());
    }
    @Test
    public void testArrayAndListTabulatedFunctionCombination() {
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues1, yValues1);
        double[] xValues2 = {0.5, 1.5, 2.5};
        double[] yValues2 = {5.0, 15.0, 25.0};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues2, yValues2);
        MathFunction composite = arrayFunc.andThen(listFunc);
        assertEquals(100.0, composite.apply(1.0), 1e-9);
    }
    @Test
    public void testTabulatedFunctionWithIdentityFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction tabulatedFunc = new LinkedListTabulatedFunction(xValues, yValues);
        MathFunction identityFunc = new IdentityFunction();
        MathFunction composite = tabulatedFunc.andThen(identityFunc);
        assertEquals(10.0, composite.apply(1.0), 1e-9);
        assertEquals(20.0, composite.apply(2.0), 1e-9);
        assertEquals(30.0, composite.apply(3.0), 1e-9);
    }

    @Test
    void testInsertable(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(25, func.apply(2.5));
        func.insert(2.5, 27.5);
        assertEquals(27.5, func.apply(2.5));
        assertEquals(0, func.apply(0));
        func.insert(0, 5);
        assertEquals(5, func.apply(0));
    }
}