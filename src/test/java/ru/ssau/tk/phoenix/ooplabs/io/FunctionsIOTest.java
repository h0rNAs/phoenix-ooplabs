package ru.ssau.tk.phoenix.ooplabs.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.SqrFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsIOTest {

    @Test
    void writerAndReader_LinkedList() {
        TabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), -4, 4, 9);
        try (FileWriter writer = new FileWriter("temp/linked list.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             FileReader reader = new FileReader("temp/linked list.txt");
             BufferedReader bufferedReader = new BufferedReader(reader);)
        {
            FunctionsIO.writeTabulatedFunction(bufferedWriter, func);
            TabulatedFunction newFunc = FunctionsIO.readTabulatedFunction(bufferedReader, new LinkedListTabulatedFunctionFactory());
            assertEquals(0, newFunc.apply(0));
            assertEquals(16, newFunc.apply(4));
            assertEquals(23, newFunc.apply(5));
            assertEquals(4, newFunc.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void writerAndReader_Array() {
        TabulatedFunction func = new ArrayTabulatedFunction(new SqrFunction(), -4, 4, 9);
        try (FileWriter writer = new FileWriter("temp/array.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             FileReader reader = new FileReader("temp/array.txt");
             BufferedReader bufferedReader = new BufferedReader(reader);)
        {
            FunctionsIO.writeTabulatedFunction(bufferedWriter, func);
            TabulatedFunction newFunc = FunctionsIO.readTabulatedFunction(bufferedReader, new ArrayTabulatedFunctionFactory());
            assertEquals(0, newFunc.apply(0));
            assertEquals(16, newFunc.apply(4));
            assertEquals(23, newFunc.apply(5));
            assertEquals(4, newFunc.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void bufferedOutputAndInput_LinkedList() {
        TabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), -4, 4, 9);
        try (FileOutputStream output = new FileOutputStream("temp/linked list.bin");
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
             FileInputStream input = new FileInputStream("temp/linked list.bin");
             BufferedInputStream bufferedInput = new BufferedInputStream(input))
        {
            FunctionsIO.writeTabulatedFunction(bufferedOutput, func);
            TabulatedFunction newFunc = FunctionsIO.readTabulatedFunction(bufferedInput, new LinkedListTabulatedFunctionFactory());
            assertEquals(0, newFunc.apply(0));
            assertEquals(16, newFunc.apply(4));
            assertEquals(23, newFunc.apply(5));
            assertEquals(4, newFunc.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void bufferedOutputAndInput_Array() {
        TabulatedFunction func = new ArrayTabulatedFunction(new SqrFunction(), -4, 4, 9);
        try (FileOutputStream output = new FileOutputStream("temp/array.bin");
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
             FileInputStream input = new FileInputStream("temp/array.bin");
             BufferedInputStream bufferedInput = new BufferedInputStream(input))
        {
            FunctionsIO.writeTabulatedFunction(bufferedOutput, func);
            TabulatedFunction newFunc = FunctionsIO.readTabulatedFunction(bufferedInput, new ArrayTabulatedFunctionFactory());
            assertEquals(0, newFunc.apply(0));
            assertEquals(16, newFunc.apply(4));
            assertEquals(23, newFunc.apply(5));
            assertEquals(4, newFunc.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serializeAndDeserialize_LinkedList(){
        try (FileOutputStream outputStream = new FileOutputStream("temp/serialized linked list.bin");
             BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream))
        {
            TabulatedFunction func = new LinkedListTabulatedFunction(new SqrFunction(), -4, 4, 9);
            FunctionsIO.serialize(bufferedStream, func);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream inputStream = new FileInputStream("temp/serialized linked list.bin");
             BufferedInputStream bufferedStream = new BufferedInputStream(inputStream))
        {
            TabulatedFunction func = FunctionsIO.deserialize(bufferedStream);
            assertEquals(0, func.apply(0));
            assertEquals(16, func.apply(4));
            assertEquals(23, func.apply(5));
            assertEquals(4, func.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serializeAndDeserialize_Array(){
        try (FileOutputStream outputStream = new FileOutputStream("temp/serialized array.bin");
             BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream))
        {
            TabulatedFunction func = new ArrayTabulatedFunction(new SqrFunction(), -4, 4, 9);
            FunctionsIO.serialize(bufferedStream, func);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream inputStream = new FileInputStream("temp/serialized array.bin");
             BufferedInputStream bufferedStream = new BufferedInputStream(inputStream))
        {
            TabulatedFunction func = FunctionsIO.deserialize(bufferedStream);
            assertEquals(0, func.apply(0));
            assertEquals(16, func.apply(4));
            assertEquals(23, func.apply(5));
            assertEquals(4, func.apply(-2));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void clearTempDirectory(){
        File[] files = new File("temp").listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            file.delete();
        }
    }
}