package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class ReadTask implements Runnable {
    private TabulatedFunction func;
    private Object lock;

    public ReadTask(TabulatedFunction func, Object lock) {
        this.func = func;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < func.getCount(); i++) {
            synchronized (lock){
                System.out.printf("After read: i = %d, x = %f, y = %f\n", i, func.getX(i), func.getY(i));
            }
        }
    }
}
