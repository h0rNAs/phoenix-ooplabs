package ru.ssau.tk.phoenix.ooplabs.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import ru.ssau.tk.phoenix.ooplabs.dto.FunctionDefinition;

@Entity
@Table(name = "functions")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "function_type")
    private String type;
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private FunctionDefinition definition;



    public Function(Long id, Long userId, String name, String type, FunctionDefinition definition) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public Function(Long userId, String name, String type, FunctionDefinition definition) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public Function() {}

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FunctionDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(FunctionDefinition definition) {
        this.definition = definition;
    }

}
