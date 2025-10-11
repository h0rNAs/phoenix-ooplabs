package ru.ssau.tk.phoenix.ooplabs.concurrent;

import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.UnitFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) {
        UnitFunction unitFunction = new UnitFunction();
        TabulatedFunction function = new LinkedListTabulatedFunction(unitFunction, 1, 1000, 1000);
        List<Thread> threads = new ArrayList<>();
        Object lock = new Object();
        Set<MultiplyingTask> tasks = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 10; i++) {
            MultiplyingTask task = new MultiplyingTask(function, lock);
            Thread thread = new Thread(task, "- " + (i + 1));
            threads.add(thread);
        }
        System.out.println("\nStarted: " + threads.size() + " threads.");
        for (Thread thread : threads) {
            thread.start();
        }
        while (!tasks.isEmpty()) {
            for (MultiplyingTask task : tasks) {
                if (task.isCompleted()) {
                    tasks.remove(task);
                }
            }
        }
        System.out.println("\nResult: " + function.toString());
    }
}