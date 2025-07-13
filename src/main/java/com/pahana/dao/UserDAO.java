package com.pahana.dao;

import com.pahana.model.User;
import java.sql.*;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean validate(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
user.setUserRole(rs.getString("userRole")); 
            return true;
        }
        return false;
    }
}
