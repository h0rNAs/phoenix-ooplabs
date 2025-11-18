package ru.ssau.tk.phoenix.ooplabs.util;

public enum FunctionType {
    SIMPLE, TABULATED, COMPOSITE;
    public static FunctionType fromString(String str){
        return FunctionType.valueOf(str);
    }
}
