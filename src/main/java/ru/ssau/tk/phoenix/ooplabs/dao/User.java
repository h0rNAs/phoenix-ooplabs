package ru.ssau.tk.phoenix.ooplabs.dao;

public class User {
    private final Long id;
    private String username;
    private String password;


    public User(){
        id = 0L;
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password){
        this(0L, username, password);
    }

    public User(Long id, User user){
        this(id, user.getUsername(), user.getPassword());
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
