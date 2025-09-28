package ru.ssau.tk.phoenix.ooplabs.operations;

import ru.ssau.tk.phoenix.ooplabs.functions.MathFunction;

public interface DifferentialOperator<T extends MathFunction> {
    T derive(T function);
}
