package ru.ssau.tk.phoenix.ooplabs.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private String definition;



    public Function(Long id, Long userId, String name, String type, String definition) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public Function(Long userId, String name, String type, String definition) {
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
