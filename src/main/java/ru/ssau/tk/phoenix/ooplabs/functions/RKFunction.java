package ru.ssau.tk.phoenix.ooplabs.functions;

public class RKFunction {

    private double x0;
    private double y0;
    private double step;
    private int steps;
    private MathFunction derivative;
    private double[] solutions; // массив значений y

    public RKFunction(double x0, double y0, double step, int steps, MathFunction derivative) {
        this.x0 = x0;
        this.y0 = y0;
        this.step = step;
        this.steps = steps;
        this.derivative = derivative;
        this.solutions = solve(); // сразу вычисляем при создании
    }
    private double[] solve() {
        double[] yValues = new double[steps + 1];
        yValues[0] = y0;
        double x = x0;
        double y = y0;

        for (int i = 1; i <= steps; i++) {
            double k1 = derivative.apply(x);
            double k2 = derivative.apply(x + step / 2.0);
            double k3 = derivative.apply(x + step / 2.0);
            double k4 = derivative.apply(x + step);
            y = y + (step / 6.0) * (k1 + 2 * k2 + 2 * k3 + k4);
            x = x0 + i * step;
            yValues[i] = y;
        }
        return yValues;
    }
    public static RKFunction forEquation(MathFunction f, double x0, double y0, double xEnd, int steps) {
        double step = (xEnd - x0) / steps;
        return new RKFunction(x0, y0, step, steps, f);
    }
    public double getValueAt(double targetX) {
        if (targetX < x0) {
            return y0;
        }
        if (targetX > x0 + steps * step) {
            return solutions[steps];
        }

        int index = (int) Math.round((targetX - x0) / step);
        index = Math.min(index, steps);
        return solutions[index];
    }
    public double[] getSolutions() {
        return solutions;
    }
    public double[] getXPoints() {
        double[] xPoints = new double[steps + 1];
        for (int i = 0; i <= steps; i++) {
            xPoints[i] = x0 + i * step;
        }
        return xPoints;
    }
    public double getX0() { return x0; }
    public double getY0() { return y0; }
    public double getStep() { return step; }
    public int getSteps() { return steps; }
    public MathFunction getDerivative() { return derivative; }
    @Override
    public String toString() {
        return "RKFunction{" +
                "x0=" + x0 +
                ", y0=" + y0 +
                ", step=" + step +
                ", steps=" + steps +
                '}';
    }
}