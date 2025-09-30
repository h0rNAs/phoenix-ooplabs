package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        try(FileOutputStream arrayOutputStream = new FileOutputStream("output/array function.bin");
            BufferedOutputStream arrayBufferedStream = new BufferedOutputStream(arrayOutputStream);
            FileOutputStream linkedListOutputStream = new FileOutputStream("output/linked list function.bin");
            BufferedOutputStream linkedListBufferedStream = new BufferedOutputStream(linkedListOutputStream))
        {
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(new SqrFunction(), -6, 6, 13);
            FunctionsIO.writeTabulatedFunction(arrayBufferedStream, arrayFunc);

            TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(new SqrFunction(), -6, 6, 13);
            FunctionsIO.writeTabulatedFunction(linkedListBufferedStream, linkedListFunc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
