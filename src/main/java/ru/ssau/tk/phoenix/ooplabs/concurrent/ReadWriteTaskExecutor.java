package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.ConstantFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        Object lock = new Object();

        TabulatedFunction func = new LinkedListTabulatedFunction(new ConstantFunction(-1), 1, 10000, 10000);
        Thread read = new Thread(new ReadTask(func, lock));
        Thread write = new Thread(new WriteTask(func, 0.5, lock));

        write.start();
        read.start();
    }
}
