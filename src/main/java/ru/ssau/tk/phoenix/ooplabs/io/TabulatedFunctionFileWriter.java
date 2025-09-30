package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {2.0, 4.0, 6.0, 8.0, 10.0};
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);
        try (
                FileWriter arrayFileWriter = new FileWriter("output/array function.txt");
                BufferedWriter arrayBufferedWriter = new BufferedWriter(arrayFileWriter);
                FileWriter linkedListFileWriter = new FileWriter("output/linked list function.txt");
                BufferedWriter linkedListBufferedWriter = new BufferedWriter(linkedListFileWriter)
        )
        {
            FunctionsIO.writeTabulatedFunction(arrayBufferedWriter, arrayFunction);
            FunctionsIO.writeTabulatedFunction(linkedListBufferedWriter, linkedListFunction);
            System.out.println("Files is successfully written:");
            System.out.println("- output/array function.txt");
            System.out.println("- output/linked list function.txt");

        }
        catch (IOException e) {
            System.err.println("Writing to file failed");
            e.printStackTrace();
        }
    }
}