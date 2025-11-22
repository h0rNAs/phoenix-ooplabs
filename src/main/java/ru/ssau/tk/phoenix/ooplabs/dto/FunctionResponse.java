package ru.ssau.tk.phoenix.ooplabs.dto;

import ru.ssau.tk.phoenix.ooplabs.util.FunctionType;

public class FunctionResponse {
    private Long id;
    private Long userId;
    private String name;
    private FunctionType type;
    private String definition;


    public FunctionResponse() {
    }

    public FunctionResponse(Long id, Long userId, String name, FunctionType type, String definition) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public FunctionResponse(Long id, Long userId, String name, String type, String definition) {
        this(id, userId, name, FunctionType.fromString(type), definition);
    }

    public FunctionResponse(Long id, FunctionRequest func) {
        this(id, func.getUserId(), func.getName(), func.getType(), func.getDefinition());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Function{" +
                "id=" + id +
                ", userId=" + userId +
                ", name=" + name +
                ", type=" + type +
                '}';
    }
}