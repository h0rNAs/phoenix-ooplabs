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
        if (knots.length != controlPoints.length + degree + 1) {
            throw new IllegalArgumentException("Неверная размерность: knots.length должно быть равно controlPoints.length + degree + 1");
        }

        this.knots = knots.clone();
        this.controlPoints = controlPoints.clone();
        this.degree = degree;
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
        // Находим интервал, в который попадает x
        int k = findKnotInterval(x);

        if (k < degree) {
            return controlPoints[0]; // Левая граница
        }
        if (k >= controlPoints.length) {
            return controlPoints[controlPoints.length - 1]; // Правая граница
        }

        // Инициализируем массив для алгоритма де Бура
        double[] d = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            d[i] = controlPoints[k - degree + i];
        }

        // Выполняем алгоритм де Бура
        for (int r = 1; r <= degree; r++) {
            for (int j = degree; j >= r; j--) {
                int i = k - degree + j;
                double alpha = (x - knots[i]) / (knots[i + degree - r + 1] - knots[i]);
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
        // Проверка границ
        if (x <= knots[degree]) {
            return degree;
        }
        if (x >= knots[knots.length - degree - 1]) {
            return knots.length - degree - 2;
        }

        // Бинарный поиск интервала
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

        return degree; // fallback
    }

    /**
     * Вспомогательный метод для создания равномерного узлового вектора
     * @param n количество контрольных точек
     * @param degree степень сплайна
     * @param a левая граница
     * @param b правая граница
     * @return равномерный узловой вектор
     */
    public static double[] createUniformKnots(int n, int degree, double a, double b) {
        int totalKnots = n + degree + 1;
        double[] knots = new double[totalKnots];

        // Левые повторяющиеся узлы
        for (int i = 0; i <= degree; i++) {
            knots[i] = a;
        }

        // Внутренние равномерные узлы
        for (int i = degree + 1; i < n; i++) {
            double t = (double) (i - degree) / (n - degree);
            knots[i] = a + t * (b - a);
        }

        // Правые повторяющиеся узлы
        for (int i = n; i < totalKnots; i++) {
            knots[i] = b;
        }

        return knots;
    }
}
