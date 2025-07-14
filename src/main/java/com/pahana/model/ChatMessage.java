package com.pahana.model;

import java.sql.Timestamp;

public class ChatMessage {
    private int id;
    private String username;
    private String role;
    private String message;
    private Timestamp timestamp;

    public ChatMessage() {
    }

    public ChatMessage(int id, String username, String role, String message, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage(String username, String role, String message) {
        this.username = username;
        this.role = role;
        this.message = message;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
