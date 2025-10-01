package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;
import ru.ssau.tk.phoenix.ooplabs.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }
    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException{
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.println(function.getCount());
        for(Point point : function){
            printWriter.printf("%f %f\n", point.x, point.y);
        }
        printWriter.flush();
    }
    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException{
        DataOutputStream stream = new DataOutputStream(outputStream);
        stream.writeInt(function.getCount());
        for (Point point : function){
            stream.writeDouble(point.x);
            stream.writeDouble(point.y);
        }
        stream.flush();
    }
    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        try {
            String countLine = reader.readLine();
            if (countLine == null) {
                throw new IOException("File is empty");
            }
            int count = Integer.parseInt(countLine.trim());
            double[] xValues = new double[count];
            double[] yValues = new double[count];
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.forLanguageTag("ru"));
            for (int i = 0; i < count; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file: expected " + count + " points but got " + i);
                }
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    throw new IOException("Invalid format in line " + (i + 2) + ": expected two values separated by space");
                }
                try {
                    xValues[i] = numberFormat.parse(parts[0]).doubleValue();
                    yValues[i] = numberFormat.parse(parts[1]).doubleValue();
                } catch (ParseException e) {
                    throw new IOException("Error parsing numbers in line " + (i + 2) + ": " + e.getMessage(), e);
                }
            }
            return factory.create(xValues, yValues);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing number of points: " + e.getMessage(), e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream dataStream = new DataInputStream(inputStream);
        int length = dataStream.readInt();
        double[] xValues = new double[length];
        double[] yValues = new double[length];

        for (int i = 0; i < length; i++) {
            xValues[i] = dataStream.readDouble();
            yValues[i] = dataStream.readDouble();
        }

        return factory.create(xValues, yValues);
    }
}
