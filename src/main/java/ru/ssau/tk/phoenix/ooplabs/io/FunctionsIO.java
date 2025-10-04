package ru.ssau.tk.phoenix.ooplabs.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import ru.ssau.tk.phoenix.ooplabs.functions.ArrayTabulatedFunction;
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
    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(function);
        objectOutputStream.flush();
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectStream = new ObjectInputStream(stream);
        return (TabulatedFunction)objectStream.readObject();
    }
    public static void serializeXml(BufferedWriter writer, ArrayTabulatedFunction function) throws IOException {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY); // Настройка безопасности для XStream
        String xml = xstream.toXML(function); // Сериализуем функцию в XML строку
        writer.write(xml);
        writer.flush();
    }
    public static ArrayTabulatedFunction deserializeXml(BufferedReader reader) throws IOException {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        StringBuilder xmlBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            xmlBuilder.append(line);
        }
        String xml = xmlBuilder.toString();
        return (ArrayTabulatedFunction) xstream.fromXML(xml);
    }

    public static void serializeJson(BufferedWriter writer, ArrayTabulatedFunction function) throws IOException {
        ObjectMapper json = new ObjectMapper();
        writer.write(json.writeValueAsString(function));
    }

    public static ArrayTabulatedFunction deserializeJson(BufferedReader reader) throws IOException {
        ObjectMapper json = new ObjectMapper();
        var object = json.readerFor(ArrayTabulatedFunction.class);
        return object.readValue(reader);
    }
}
