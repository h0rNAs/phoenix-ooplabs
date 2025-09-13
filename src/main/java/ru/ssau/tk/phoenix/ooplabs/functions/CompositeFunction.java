package ru.ssau.tk.phoenix.ooplabs.functions;

public class CompositeFunction implements MathFunction {
    MathFunction f, g;  // f выполняется первой, а g - второй

    public CompositeFunction(MathFunction f, MathFunction g) {
        this.f = f;
        this.g = g;
    }

    @Override
    public double apply(double x) {
        return g.apply(f.apply(x));
    }
}
