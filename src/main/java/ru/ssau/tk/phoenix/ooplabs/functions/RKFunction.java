package ru.ssau.tk.phoenix.ooplabs.functions;

public class RKFunction implements MathFunction {

    double x0, y0;
    double step;

    public RKFunction(double x0, double y0, double step) {
        this.x0 = x0;
        this.y0 = y0;
        this.step = step;
    }

    @Override
    public double apply(double x) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step size must be positive");
        }

        double currentX = x0;
        double currentY = y0;

        while (Math.abs(x - currentX) > 1e-10) {
            if (Math.abs(step) > Math.abs(x - currentX)) {
                step = x - currentX;
            }

            double k1 = currentX;
            double k2 = currentX + step / 2;
            double k3 = currentX + step / 2;
            double k4 = currentX + step;

            currentY += (step / 6) * (k1 + 2 * k2 + 2 * k3 + k4);
            currentX += step;
        }
        return currentY;
    }
}
