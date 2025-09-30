package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.io.*;

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
}
