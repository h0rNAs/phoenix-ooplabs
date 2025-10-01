package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.operations.DifferentialOperator;
import ru.ssau.tk.phoenix.ooplabs.operations.TabulatedDifferentialOperator;

import java.io.*;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (FileInputStream inputStream = new FileInputStream("input/binary function.bin");
             BufferedInputStream bufferedInput = new BufferedInputStream(inputStream))
        {
            TabulatedFunction func = FunctionsIO.readTabulatedFunction(bufferedInput, new ArrayTabulatedFunctionFactory());
            System.out.println(func.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedInput = new BufferedReader(streamReader);

            System.out.println("Введите размер и значения функции");

            TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
            TabulatedFunction func = FunctionsIO.readTabulatedFunction(bufferedInput, factory);
            TabulatedFunction differentialFunc = new TabulatedDifferentialOperator(factory).derive(func);

            System.out.println(differentialFunc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
