package ru.ssau.tk.phoenix.ooplabs.dto;

import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;

public class FunctionRequest {
    private Long userId;
    private String name;
    private FunctionType type;
    private FunctionDefinition definition;


    public FunctionRequest(Long userId, String name, FunctionType type, FunctionDefinition definition) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FunctionType getType() {
        return type;
    }

    public void setType(FunctionType type) {
        this.type = type;
    }

    public FunctionDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(FunctionDefinition definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "FunctionRequest{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}



/*
{
    "userId": 1,
    "name": "func",
    "type": "TABULATED",
    "definition": {
        "edits": []
    }
}
 */