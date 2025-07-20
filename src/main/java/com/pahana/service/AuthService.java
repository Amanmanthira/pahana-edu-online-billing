package com.pahana.service;

import com.pahana.model.User;
import java.sql.*;

public class AuthService {
    private Connection conn;

    public AuthService(Connection conn) {
        this.conn = conn;
    }

 public User login(User user) throws SQLException {
    String sql = "SELECT * FROM users WHERE username=? AND password=?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, user.getUsername());
    ps.setString(2, user.getPassword());
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
String role = rs.getString("userRole");
        return new User(user.getUsername(), user.getPassword(), role);
    }

    return null;
}


   public boolean register(User user) throws SQLException {
    // Check if user exists
    String check = "SELECT * FROM users WHERE username=?";
    PreparedStatement checkStmt = conn.prepareStatement(check);
    checkStmt.setString(1, user.getUsername());
    ResultSet rs = checkStmt.executeQuery();
    if (rs.next()) return false; // already exists

    // Insert new user with role
    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, user.getUsername());
    ps.setString(2, user.getPassword());
    ps.setString(3, user.getRole()); 
    ps.executeUpdate();
    return true;
}

}
