package ru.ssau.tk.phoenix.ooplabs.functions;

public class DeBoorFunction implements MathFunction{
    private double[] knots; // Узловой вектор
    private double[] controlPoints; // Контрольные точки
    private int degree; // Степень B-сплайна

    /**
     * Конструктор алгоритма де Бура
     * @param knots узловой вектор (должен быть отсортирован по возрастанию)
     * @param controlPoints контрольные точки
     * @param degree степень B-сплайна
     */
    public DeBoorFunction(double[] knots, double[] controlPoints, int degree) {
        validateParameters(knots, controlPoints, degree);

        this.knots = knots.clone();
        this.controlPoints = controlPoints.clone();
        this.degree = degree;
    }

    private void validateParameters(double[] knots, double[] controlPoints, int degree) {
        if (knots.length != controlPoints.length + degree + 1) {
            throw new IllegalArgumentException(
                    "Неверная размерность: knots.length должно быть равно controlPoints.length + degree + 1");
        }

        if (degree < 0) {
            throw new IllegalArgumentException("Степень не может быть отрицательной: " + degree);
        }

        if (controlPoints.length == 0) {
            throw new IllegalArgumentException("Массив контрольных точек не может быть пустым");
        }

        for (int i = 1; i < knots.length; i++) {
            if (knots[i] < knots[i - 1]) {
                throw new IllegalArgumentException("Узловой вектор должен быть отсортирован по возрастанию");
            }
        }
    }

    /**
     * Реализация интерфейса MathFunction - вычисление значения B-сплайна в точке x
     * @param x точка, в которой вычисляется сплайн
     * @return значение B-сплайна в точке x
     */
    @Override
    public double apply(double x) {
        return deBoor(x);
    }

    /**
     * Алгоритм де Бура для вычисления значения B-сплайна
     * @param x точка, в которой вычисляется сплайн
     * @return значение B-сплайна в точке x
     */
    private double deBoor(double x) {
        // Обработка точек вне области определения
        if (x <= knots[degree]) {
            return controlPoints[0];
        }
        if (x >= knots[knots.length - degree - 1]) {
            return controlPoints[controlPoints.length - 1];
        }

        // Находим интервал, в который попадает x
        int k = findKnotInterval(x);

        // Инициализируем массив для алгоритма де Бура
        double[] d = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            d[i] = controlPoints[k - degree + i];
        }

        // Выполняем алгоритм де Бура
        for (int r = 1; r <= degree; r++) {
            for (int j = degree; j >= r; j--) {
                int i = k - degree + j;
                double denominator = knots[i + degree - r + 1] - knots[i];

                // Избегаем деления на ноль
                if (Math.abs(denominator) < 1e-10) {
                    continue;
                }

                double alpha = (x - knots[i]) / denominator;
                d[j] = (1 - alpha) * d[j - 1] + alpha * d[j];
            }
        }

        return d[degree];
    }

    /**
     * Находит индекс интервала узлового вектора, содержащего точку x
     * @param x точка
     * @return индекс k такой, что knots[k] <= x < knots[k+1]
     */
    private int findKnotInterval(double x) {
        int low = degree;
        int high = knots.length - degree - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (knots[mid] <= x && x < knots[mid + 1]) {
                return mid;
            } else if (x < knots[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return degree;
    }
}
