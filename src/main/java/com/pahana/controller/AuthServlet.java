package com.pahana.controller;

import com.pahana.model.User;
import com.pahana.service.AuthService;
import com.pahana.util.DBConnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain");
        res.getWriter().write("AuthServlet is running!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String u = req.getParameter("username");
        String p = req.getParameter("password");

        try {
            Connection conn = DBConnection.getInstance();
            AuthService auth = new AuthService(conn);
            User user = auth.login(new User(u, p));

            if (user != null) {
                res.setContentType("application/json");
                res.getWriter().write("{\"message\":\"Login successful\", \"role\":\"" + user.getUserRole() + "\", \"username\":\"" + user.getUsername() + "\"}");
            } else {
                res.sendError(401, "Invalid credentials");
            }

        } catch (ClassNotFoundException e) {
            res.sendError(500, "MySQL Driver not found: " + e.getMessage());
        } catch (IOException | SQLException e) {
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }
}
