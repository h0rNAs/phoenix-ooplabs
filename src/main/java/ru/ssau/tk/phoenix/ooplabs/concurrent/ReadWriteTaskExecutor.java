package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.ConstantFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction func = new LinkedListTabulatedFunction(new ConstantFunction(-1), 1, 1000, 1000);
        Thread write = new Thread(new WriteTask(func, 0.5));
        Thread read = new Thread(new ReadTask(func));

        write.start();
        read.start();
    }
}
