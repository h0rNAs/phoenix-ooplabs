package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.io.*;

public class ArrayTabulatedFunctionJsonSerialization {
    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("output/serialized array function.json");
             BufferedWriter bufferedWriter = new BufferedWriter(writer))
        {
            ArrayTabulatedFunction func = new ArrayTabulatedFunction(new SqrFunction(), -4, 4, 9);
            FunctionsIO.serializeJson(bufferedWriter, func);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileReader reader = new FileReader("output/serialized array function.json");
             BufferedReader bufferedReader = new BufferedReader(reader))
        {
            TabulatedFunction func = FunctionsIO.deserializeJson(bufferedReader);
            System.out.println(func.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
