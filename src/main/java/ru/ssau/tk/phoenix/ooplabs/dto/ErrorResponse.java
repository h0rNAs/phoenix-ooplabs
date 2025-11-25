package ru.ssau.tk.phoenix.ooplabs.dto;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

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

    public static ErrorResponse constructIllegalArgumentException(IllegalArgumentException e){
        return new ErrorResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                "Bad Request",
                e.getMessage()
        );
    }

    public static ErrorResponse constructNoSuchElementException(NoSuchElementException e){
        return new ErrorResponse(
                HttpServletResponse.SC_NOT_FOUND,
                "Not Found",
                e.getMessage()
        );
    }

    public static ErrorResponse constructException(Exception e){
        return new ErrorResponse(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                e.getMessage()
        );
    }
}