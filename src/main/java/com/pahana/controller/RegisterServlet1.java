package com.pahana.controller;

import com.pahana.model.User;
import com.pahana.service.AuthService;
import com.pahana.util.DBConnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class RegisterServlet1 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String u = req.getParameter("username");
        String p = req.getParameter("password");

        try {
            Connection conn = DBConnection.getInstance();
            AuthService auth = new AuthService(conn);
            if (auth.register(new User(u, p))) {
                res.setContentType("application/json");
                res.getWriter().write("{\"message\":\"User registered successfully\"}");
            } else {
                res.sendError(400, "Username already exists");
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }
}
