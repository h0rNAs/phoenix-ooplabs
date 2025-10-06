package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class MultiplyingTask implements Runnable{
    private final TabulatedFunction function;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            double currentY = function.getY(i);
            function.setY(i, currentY * 2);
            
        }
        System.out.printf("\nStream " + Thread.currentThread().getName() + " ended successfully.");
    }

}
