package com.pahana.controller;

import com.pahana.model.User;
import com.pahana.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.List;

public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = new User(
            req.getParameter("username"),
            req.getParameter("password"),
            req.getParameter("userRole")
        );

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            UserService service = new UserService(conn);
            service.addUser(user);
            res.getWriter().write("User added.");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader = req.getReader();
        String[] data = reader.readLine().split("&");

        String username = data[0].split("=")[1];
        String password = data[1].split("=")[1];
        String userRole = data[2].split("=")[1];

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            UserService service = new UserService(conn);
            service.updateUser(new User(username, password, userRole));
            res.getWriter().write("User updated.");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String username = req.getParameter("username");

        if (username == null || username.isEmpty()) {
            res.sendError(400, "Missing username");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            UserService service = new UserService(conn);
            boolean deleted = service.deleteUser(username);
            if (deleted) {
                res.getWriter().write("User deleted.");
            } else {
                res.sendError(404, "User not found");
            }
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String username = req.getParameter("username");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            UserService service = new UserService(conn);
            res.setContentType("application/json");

            if (username == null || username.isEmpty()) {
                List<User> users = service.findAllUsers();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
                    sb.append("{\"username\":\"").append(u.getUsername())
                      .append("\",\"password\":\"").append(u.getPassword())
                      .append("\",\"userRole\":\"").append(u.getUserRole()).append("\"}");
                    if (i < users.size() - 1) sb.append(",");
                }
                sb.append("]");
                res.getWriter().write(sb.toString());
            } else {
                User u = service.findUser(username);
                if (u != null) {
                    res.getWriter().write("{\"username\":\"" + u.getUsername() +
                        "\",\"password\":\"" + u.getPassword() +
                        "\",\"userRole\":\"" + u.getUserRole() + "\"}");
                } else {
                    res.sendError(404, "User not found");
                }
            }
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }
}
