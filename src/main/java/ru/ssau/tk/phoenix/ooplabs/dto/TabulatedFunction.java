package ru.ssau.tk.phoenix.ooplabs.dto;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;

public class TabulatedFunction extends FunctionDefinition {
    public TabulatedFunction(Point[] edits) {
        this.points = edits;
    }

    public TabulatedFunction() {
    }
}
