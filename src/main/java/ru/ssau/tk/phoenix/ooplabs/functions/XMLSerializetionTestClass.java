package ru.ssau.tk.phoenix.ooplabs.functions;
import ru.ssau.tk.phoenix.ooplabs.io.FunctionsIO;
import java.io.*;

public class XMLSerializetionTestClass {
    public static void main(String[] args) {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        try (FileWriter fileWriter = new FileWriter("output/function.xml");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            FunctionsIO.serializeXml(bufferedWriter, function);
            System.out.println("Function serialized in XML file: output/function.xml");
        } catch (IOException e) {
            System.err.println("Error in XML function serialization:");
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("output/function.xml");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            ArrayTabulatedFunction deserializedFunction = FunctionsIO.deserializeXml(bufferedReader);
            System.out.println("Function deserialized from XML file: output/function.xml");
            System.out.println("\nDeserialized function:");
            System.out.println("Number of points: " + deserializedFunction.getCount());
            System.out.println("Left border: " + deserializedFunction.leftBound());
            System.out.println("Right border: " + deserializedFunction.rightBound());
            System.out.println("\nPoints of function:");
            for (int i = 0; i < deserializedFunction.getCount(); i++) {
                System.out.printf("Point %d: (%.1f, %.1f)%n",
                        i, deserializedFunction.getX(i), deserializedFunction.getY(i));
            }
            System.out.println("\nCheck of functionality:");
            System.out.printf("f(2.5) = %.2f%n", deserializedFunction.apply(2.5));
            System.out.printf("f(0.0) = %.2f (left extrapolation)%n", deserializedFunction.apply(0.0));
            System.out.printf("f(6.0) = %.2f (right extrapolation)%n", deserializedFunction.apply(6.0));

        } catch (IOException e) {
            System.err.println("Error in XML deserialization of function:");
            e.printStackTrace();
        }
    }
}
