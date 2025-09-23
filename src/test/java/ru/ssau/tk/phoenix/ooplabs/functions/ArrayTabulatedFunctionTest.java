package ru.ssau.tk.phoenix.ooplabs.functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ArrayTabulatedFunctionTest {
    @Test
    public void testConstructorWithMathFunction() {
        MathFunction source = new SqrFunction();
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(source, 0.0, 4.0, 5);
        assertEquals(function.getCount(), 5);
        assertEquals(function.getX(0), 0.0);
        assertEquals(function.getY(0), 0.0);
        assertEquals(function.getX(1), 1.0);
        assertEquals(function.getY(1), 1.0);
        assertEquals(function.getX(2), 2.0);
        assertEquals(function.getY(2), 4.0);
        assertEquals(function.getX(3), 3.0);
        assertEquals(function.getY(3), 9.0);
        assertEquals(function.getX(4), 4.0);
        assertEquals(function.getY(4), 16.0);
    }
    @Test
    public void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(function.apply(1.0), 10.0);
        assertEquals(function.apply(2.0), 20.0);
        assertEquals(function.apply(3.0), 30.0);//Интерполяция
        assertEquals(function.apply(1.5), 15.0);
        assertEquals(function.apply(2.5), 25.0);//Экстраполяция слева
        assertEquals(function.apply(0.5), 5.0);//Экстраполяция справа
        assertEquals(function.apply(3.5), 35.0);
    }
    @Test
    public void testApplyWithOnePoint() {
        double[] xValues = {5.0};
        double[] yValues = {10.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);// Всегда возвращает yValues[0]
        assertEquals(function.apply(0.0), 10.0);
        assertEquals(function.apply(5.0), 10.0);
        assertEquals(function.apply(10.0), 10.0);
    }
    @Test
    public void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(function.indexOfX(1.0), 0);
        assertEquals(function.indexOfX(2.0), 1);
        assertEquals(function.indexOfX(3.0), 2);
        assertEquals(function.indexOfX(4.0), -1);
    }
    @Test
    public void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(function.indexOfY(10.0), 0);
        assertEquals(function.indexOfY(20.0), 1);
        assertEquals(function.indexOfY(30.0), 2);
        assertEquals(function.indexOfY(40.0), -1);
    }
    @Test
    public void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.setY(1, 25.0);
        assertEquals(function.getY(1), 25.0);
    }
    @Test
    public void testLeftAndRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(function.leftBound(), 1.0);
        assertEquals(function.rightBound(), 3.0);
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