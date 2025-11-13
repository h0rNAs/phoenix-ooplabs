package ru.ssau.tk.phoenix.ooplabs.dto;

import java.util.List;

public class UserRequest {
    private String username;
    private String password;
    private List<FunctionRequest> functions;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
        //this.functions = functions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FunctionRequest> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionRequest> functions) {
        this.functions = functions;
    }
}
