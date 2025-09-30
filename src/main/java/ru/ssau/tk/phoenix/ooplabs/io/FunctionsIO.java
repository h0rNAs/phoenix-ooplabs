package ru.ssau.tk.phoenix.ooplabs.io;

import ru.ssau.tk.phoenix.ooplabs.functions.Point;
import ru.ssau.tk.phoenix.ooplabs.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
}
