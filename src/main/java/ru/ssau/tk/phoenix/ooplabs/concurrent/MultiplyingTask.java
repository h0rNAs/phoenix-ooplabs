package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class MultiplyingTask implements Runnable{
    private final TabulatedFunction function;
    private Object lock;

    public MultiplyingTask(TabulatedFunction function, Object lock) {
        this.function = function;
        this.lock = lock;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (lock) {
                double currentY = function.getY(i);
                function.setY(i, currentY * 2);
            }
        }
        System.out.printf("\nStream " + Thread.currentThread().getName() + " ended successfully.");
    }
}
