package ru.ssau.tk.phoenix.ooplabs.dao;

public class Function {
    public enum FunctionType{
        SIMPLE, TABULATED, COMPOSITE;
        public static FunctionType fromString(String str){
            return FunctionType.valueOf(str);
        }
    }

    private final Long id;
    private final Long userId;
    private FunctionType type;
    private String definition;


    public Function(Long id, Long userId, FunctionType type, String definition) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.definition = definition;
    }

    public Function(Long id, Long userId, String type, String definition) {
        this(id, userId, FunctionType.fromString(type), definition);
    }

    public Function(Long id, Function func){
        this(id, func.getUserId(), func.getType(), func.getDefinition());
    }

    public Function(Long userId, FunctionType type, String definition){
        this(0L, userId, type, definition);
    }

    public Function(){
        this(0L, 0L, FunctionType.SIMPLE, "");
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
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
                ", type=" + type +
                '}';
    }
}