package ru.ssau.tk.phoenix.ooplabs.dto;

import java.util.List;

public class ErrorResponse {
    private int code;
    private String message;
    private List<String> error;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(int code, String message, List<String> error) {
        this(code, message);
        this.error = error;
    }

    public ErrorResponse(int code, String message, String detail) {
        this(code, message, List.of(detail));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}
