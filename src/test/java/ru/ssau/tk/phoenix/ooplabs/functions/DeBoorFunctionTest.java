package ru.ssau.tk.phoenix.ooplabs.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeBoorFunctionTest {
    @Test
    void apply_linearSpline() {
        // Линейный B-сплайн (степень 1)
        double[] knots = {0, 0, 1, 2, 2};
        double[] controlPoints = {1.0, 3.0, 2.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 1);

        assertEquals(1.0, deBoor.apply(0.0), 1e-10);
        assertEquals(2.0, deBoor.apply(0.5), 1e-10);
        assertEquals(3.0, deBoor.apply(1.0), 1e-10);
        assertEquals(2.5, deBoor.apply(1.5), 1e-10);
        assertEquals(2.0, deBoor.apply(2.0), 1e-10);
    }

    @Test
    void apply_quadraticSpline() {
        // Квадратичный B-сплайн (степень 2) - пример из предыдущего сообщения
        double[] knots = {0, 0, 0, 1, 2, 2, 2};
        double[] controlPoints = {1.0, 2.0, 4.0, 3.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 2);

        assertEquals(1.0, deBoor.apply(0.0), 1e-10);
        assertEquals(2.0, deBoor.apply(0.5), 1e-10);
        assertEquals(3.0, deBoor.apply(1.0), 1e-10);
        assertEquals(3.5, deBoor.apply(1.5), 1e-10);
        assertEquals(3.0, deBoor.apply(2.0), 1e-10);
    }

    @Test
    void apply_cubicSpline() {
        // Кубический B-сплайн (степень 3)
        double[] knots = {0, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4};
        double[] controlPoints = {0.0, 1.0, 3.0, 2.0, 4.0, 5.0, 6.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 3);

        assertEquals(0.0, deBoor.apply(0.0), 1e-10);
        assertEquals(6.0, deBoor.apply(4.0), 1e-10);
    }

    @Test
    void apply_singlePoint() {
        // Вырожденный случай - одна контрольная точка
        double[] knots = {0, 0, 0}; // для степени 2
        double[] controlPoints = {5.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 1);

        assertEquals(5.0, deBoor.apply(0.0), 1e-10);
        assertEquals(5.0, deBoor.apply(0.5), 1e-10);
        assertEquals(5.0, deBoor.apply(1.0), 1e-10);
    }

    @Test
    void apply_extrapolation() {
        // Проверка поведения вне области определения
        double[] knots = {0, 0, 1, 1};
        double[] controlPoints = {1.0, 2.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 1);

        // Вне области определения должен возвращать крайние значения
        assertEquals(1.0, deBoor.apply(-12.0), 1e-10);
        assertEquals(2.0, deBoor.apply(50.0), 1e-10);
    }

    @Test
    void apply_invalidParameters() {
        double[] knots = {0, 0, 1, 1};
        double[] controlPoints = {1.0, 2.0};

        assertThrows(IllegalArgumentException.class, () -> {
            new DeBoorFunction(knots, controlPoints, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new DeBoorFunction(knots, new double[0], 1);
        });
    }

    @Test
    void apply_zeroDegree() {
        double[] knots = {0, 1, 2, 3};
        double[] controlPoints = {1.0, 2.0, 3.0};
        DeBoorFunction deBoor = new DeBoorFunction(knots, controlPoints, 0);

        assertEquals(1.0, deBoor.apply(0.0), 1e-10);
        assertEquals(1.0, deBoor.apply(0.5), 1e-10);
        assertEquals(2.0, deBoor.apply(1.0), 1e-10);
        assertEquals(2.0, deBoor.apply(1.5), 1e-10);
        assertEquals(3.0, deBoor.apply(2.0), 1e-10);
        assertEquals(3.0, deBoor.apply(2.5), 1e-10);
    }
}