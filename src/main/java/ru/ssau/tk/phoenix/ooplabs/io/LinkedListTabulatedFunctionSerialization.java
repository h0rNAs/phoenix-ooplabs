package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.operations.TabulatedDifferentialOperator;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try (FileOutputStream outputStream = new FileOutputStream("output/serialized linked list functions.bin");
             BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream))
        {
            TabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), -4, 4, 9);

            TabulatedDifferentialOperator op = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction firstDiffFunc = op.derive(func);
            TabulatedFunction secondDiffFunc = op.derive(firstDiffFunc);

            FunctionsIO.serialize(bufferedStream, func);
            FunctionsIO.serialize(bufferedStream, firstDiffFunc);
            FunctionsIO.serialize(bufferedStream, secondDiffFunc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream inputStream = new FileInputStream("output/serialized linked list functions.bin");
             BufferedInputStream bufferedStream = new BufferedInputStream(inputStream))
        {
            TabulatedFunction func = FunctionsIO.deserialize(bufferedStream);
            System.out.println(func.toString());
            TabulatedFunction firstDiffFunc = FunctionsIO.deserialize(bufferedStream);
            System.out.println(firstDiffFunc.toString());
            TabulatedFunction secondDiffFunc = FunctionsIO.deserialize(bufferedStream);
            System.out.println(secondDiffFunc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
