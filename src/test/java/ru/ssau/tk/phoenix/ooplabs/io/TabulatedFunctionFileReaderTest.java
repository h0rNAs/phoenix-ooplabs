package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class TabulatedFunctionFileReaderTest {

    @Test
    void FileReader_ReadTabulatedFunction() throws IOException {
        String testData = "3\n1,5 2,5\n3,5 4,5\n5,5 6,5\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = FunctionsIO.readTabulatedFunction(reader, factory);
        assertEquals(3, function.getCount());
        assertEquals(1.5, function.getX(0), 1e-10);
        assertEquals(2.5, function.getY(0), 1e-10);
        assertEquals(3.5, function.getX(1), 1e-10);
        assertEquals(4.5, function.getY(1), 1e-10);
        assertEquals(5.5, function.getX(2), 1e-10);
        assertEquals(6.5, function.getY(2), 1e-10);
    }
    @Test
    void FileReader_TabulatedFunctionWithDifferentFactory() throws IOException {
        String testData = "2\n0,0 1,0\n1,0 2,0\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction function = FunctionsIO.readTabulatedFunction(reader, factory);
        assertEquals(2, function.getCount());
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getY(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(2.0, function.getY(1), 1e-10);
    }
    @Test
    void FileReader_TabulatedFunctionWithEmptyFile() {
        String testData = "";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        IOException exception = assertThrows(IOException.class, () ->
                FunctionsIO.readTabulatedFunction(reader, factory)
        );
        assertTrue(exception.getMessage().contains("File is empty"));
    }
    @Test
    void FileReader_TabulatedFunctionWithInvalidCount() {
        String testData = "abc\n1,0 2,0\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        assertThrows(IOException.class, () ->
                FunctionsIO.readTabulatedFunction(reader, factory)
        );
    }

    @Test
    void FileReader_TabulatedFunctionWithUnexpectedEnd() {
        String testData = "3\n1,0 2,0\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        IOException exception = assertThrows(IOException.class, () ->
                FunctionsIO.readTabulatedFunction(reader, factory)
        );
        assertTrue(exception.getMessage().contains("Unexpected end of file"));
    }
    @Test
    void FileReader_TabulatedFunctionWithInvalidFormat() {
        String testData = "2\n1,0 2,0 3,0\n4,0 5,0\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        IOException exception = assertThrows(IOException.class, () ->
                FunctionsIO.readTabulatedFunction(reader, factory)
        );
        assertTrue(exception.getMessage().contains("Invalid format"));
    }
    @Test
    void FileReader_TabulatedFunctionWithInvalidNumbers() {
        String testData = "2\nabc 2,0\n3,0 def\n";
        BufferedReader reader = new BufferedReader(new StringReader(testData));
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        assertThrows(IOException.class, () ->
                FunctionsIO.readTabulatedFunction(reader, factory)
        );
    }
}