package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class WriteTask implements Runnable{
    private TabulatedFunction func;
    private Object lock;
    private double value;

    public WriteTask(TabulatedFunction func, double value, Object lock) {
        this.func = func;
        this.value = value;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < func.getCount(); i++) {
            synchronized (lock){
                func.setY(i, value);
                System.out.printf("Writing for index %d complete\n", i);
            }
        }
    }
}
