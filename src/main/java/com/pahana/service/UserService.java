package com.pahana.service;

import com.pahana.model.User;
import java.sql.*;
import java.util.*;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    public void addUser(User u) throws SQLException {
        String sql = "INSERT INTO users (username, password, userRole) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getUserRole());
        ps.executeUpdate();
    }

    public boolean updateUser(User u) throws SQLException {
        String sql = "UPDATE users SET password=?, userRole=? WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getPassword());
        ps.setString(2, u.getUserRole());
        ps.setString(3, u.getUsername());
        return ps.executeUpdate() > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        return ps.executeUpdate() > 0;
    }

    public User findUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(rs.getString("username"), rs.getString("password"), rs.getString("userRole"));
        }
        return null;
    }

    public List<User> findAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(new User(rs.getString("username"), rs.getString("password"), rs.getString("userRole")));
        }
        return list;
    }
}
