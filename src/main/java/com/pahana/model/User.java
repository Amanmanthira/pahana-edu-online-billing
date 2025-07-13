package com.pahana.model;

public class User {
    private String username;
    private String password;
    private String userRole; 

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserRole() { return userRole; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getRole() {
    return userRole;
}

}
