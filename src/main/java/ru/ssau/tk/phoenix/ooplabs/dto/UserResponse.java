package ru.ssau.tk.phoenix.ooplabs.dto;

public class UserResponse {
    private Long id;
    private String username;
    private String password;

    public UserResponse(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserResponse(Long id, UserRequest user){
        this(id, user.getUsername(), user.getPassword());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
