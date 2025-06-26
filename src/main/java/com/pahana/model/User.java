package com.pahana.model;
public class User {
    private String username, password;
    public User() {}
    public User(String u, String p) { this.username = u; this.password = p; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
