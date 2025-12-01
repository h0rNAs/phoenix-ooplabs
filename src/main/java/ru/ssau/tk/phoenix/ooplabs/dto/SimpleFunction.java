package ru.ssau.tk.phoenix.ooplabs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ssau.tk.phoenix.ooplabs.functions.Point;

public class SimpleFunction extends FunctionDefinition{
    private String function;
    private int pointsCount;
    @JsonProperty("xFrom") private double xFrom;
    @JsonProperty("xTo") private double xTo;

    public SimpleFunction(String function, int pointsCount, double xFrom, double xTo, Point[] edits) {
        this.function = function;
        this.pointsCount = pointsCount;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.points = edits;
    }

    public SimpleFunction() {
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public double getxFrom() {
        return xFrom;
    }

    public void setxFrom(double xFrom) {
        this.xFrom = xFrom;
    }

    public double getxTo() {
        return xTo;
    }

    public void setxTo(double xTo) {
        this.xTo = xTo;
    }
}
