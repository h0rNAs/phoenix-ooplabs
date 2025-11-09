package ru.ssau.tk.phoenix.ooplabs.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "functions")
public class Function {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type;
    private String name;
    private String definition;

    public Function(Long id, User user, String type, String name, String defenition) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.name = name;
        this.definition = defenition;
    }

    public Function() {

    }

    @JoinColumn(name = "user_id", nullable = false)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setDefinition(String defenition) {
        this.definition = defenition;
    }

}
