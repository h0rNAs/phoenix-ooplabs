package ru.ssau.tk.phoenix.ooplabs.operations;

import java.util.concurrent.RecursiveTask;

public class IntegralTask extends RecursiveTask<Double> {
    private final int THRESHOLD = 2;
    int startIndex, endIndex;
    IntegralOperator operator;

    public IntegralTask(int startIndex, int endIndex, IntegralOperator operator) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.operator = operator;
    }

    @Override
    protected Double compute() {
        int n = endIndex - startIndex;
        if (n <= THRESHOLD) {
            return operator.derive(startIndex, endIndex);
        }

        int middleIndex = (startIndex + endIndex) / 2;
        IntegralTask leftTask = new IntegralTask(startIndex, middleIndex, operator);
        IntegralTask rightTask = new IntegralTask(middleIndex, endIndex, operator);

        leftTask.fork();
        double rightRes = rightTask.compute();
        double leftRes = leftTask.join();

        return leftRes + rightRes;
    }
}
