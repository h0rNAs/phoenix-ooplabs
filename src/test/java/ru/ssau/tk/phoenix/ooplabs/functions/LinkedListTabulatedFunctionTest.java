package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {

    @Test
    void constructor_differentLengthArrays() {
        double[] xValues = new double[]{0, 1, 2, 3};
        double[] yValues = new double[]{5, 4, 2};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }

    @Test
    void constructor_anSortedArray(){
        double[] xValues = new double[]{0, 2, 1};
        double[] yValues = new double[]{5, 4, 2};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }

    @Test
    void constructor_smallArray(){
        double[] xValues = new double[]{0};
        double[] yValues = new double[]{5};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));
    }

    @Test
    void setY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(xValues, yValues);

        f.setY(1, 1);
        assertEquals(1, f.getY(1));
        assertThrows(IllegalArgumentException.class, () -> f.setY(-2, 1));
    }

    @Test
    void getXAndY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> func.getY(-2));
        assertThrows(IllegalArgumentException.class, () -> func.getX(3424));
    }

    @Test
    void floorIndexOfX(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1, func.floorIndexOfX(1.5));
        assertEquals(1, func.floorIndexOfX(1));
        assertThrows(IllegalArgumentException.class, () -> func.floorIndexOfX(-868));
    }

    @Test
    void floorNodeOfX(){
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1, func.floorNodeOfX(1.5).x);
        assertEquals(1, func.floorNodeOfX(1).x);
        assertThrows(IllegalArgumentException.class, () -> func.floorIndexOfX(-868));
    }

    @Test
    void indexOfX() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(2, func.indexOfX(2));
        assertEquals(-1, func.indexOfX(234));
    }

    @Test
    void indexOfY() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(1, func.indexOfY(4));
        assertEquals(-1, func.indexOfY(234));
    }

    @Test
    void leftAndRightBound() {
        double[] xValues = new double[]{2, 16, 24};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(2, func.leftBound());
        assertEquals(24, func.rightBound());
    }


    @Test
    void apply() {
        double[] xValues = new double[]{0, 1, 2};
        double[] yValues = new double[]{5, 4, 2};
        LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(2, func.apply(2));
        assertEquals(-4, func.apply(5));
        assertEquals(3, func.apply(1.5));
        assertEquals(6, func.apply(-1));
    }

    @Test
    void apply_linkedListAndArray(){
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues1, yValues1);

        double[] xValues2 = {5.0, 15.0, 25.0};
        double[] yValues2 = {0.5, 1.5, 2.5};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues2, yValues2);

        CompositeFunction func = listFunc.andThen(arrayFunc);
        assertEquals(2, func.apply(2));
        assertEquals(4, func.apply(4));
    }

    @Test
    void apply_linkedListAndSqrt(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues, yValues);
        CompositeFunction func = listFunc.andThen(new SqrFunction());
        assertEquals(100, func.apply(1));
        assertEquals(100, func.apply(-1));
    }

    @Test
    void apply_linkedListAndDeBoor(){
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {0.5, 1, 2};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues1, yValues1);

        double[] knots = {0, 0, 1, 2, 2};
        double[] controlPoints = {1.0, 3.0, 2.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 1);

        CompositeFunction func = listFunc.andThen(deBoor);
        assertEquals(3, func.apply(2));
        assertEquals(1, func.apply(0));
    }

    @Test
    void apply_linkedListAndConstant(){
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues, yValues);
        CompositeFunction func = listFunc.andThen(new ConstantFunction(5));
        assertEquals(5, func.apply(1));
        assertEquals(5, func.apply(324324));
        assertEquals(5, func.apply(-2));
    }

    @Test
    public void testInsertAtBeginning() {
        double[] xValues = {2.0, 3.0, 4.0};
        double[] yValues = {20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.insert(1.0, 10.0);
        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(10.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(20.0, function.getY(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(30.0, function.getY(2));
        assertEquals(4.0, function.getX(3));
        assertEquals(40.0, function.getY(3));
    }
    @Test
    public void testInsertAtEnd() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.insert(4.0, 40.0);
        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(10.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(20.0, function.getY(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(30.0, function.getY(2));
        assertEquals(4.0, function.getX(3));
        assertEquals(40.0, function.getY(3));
    }
    @Test
    public void testInsertInMiddle() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.insert(2.0, 20.0);
        function.insert(4.0, 40.0);
        assertEquals(5, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(10.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(20.0, function.getY(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(30.0, function.getY(2));
        assertEquals(4.0, function.getX(3));
        assertEquals(40.0, function.getY(3));
        assertEquals(5.0, function.getX(4));
        assertEquals(50.0, function.getY(4));
    }

}