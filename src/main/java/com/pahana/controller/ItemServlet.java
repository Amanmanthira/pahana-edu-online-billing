package com.pahana.controller;
import com.pahana.dao.ItemDAO;
import com.pahana.model.Item;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            List<Item> items = new ItemDAO(conn).getAll();
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.print("[");
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                out.print(String.format("{\"id\":%d,\"name\":\"%s\",\"price\":%.2f}", item.getId(), item.getName(), item.getPrice()));
                if (i < items.size() - 1) out.print(",");
            }
            out.print("]");
        } catch (Exception e) { res.sendError(500, e.getMessage()); }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            new ItemDAO(conn).add(new Item(0, name, price));
            res.getWriter().write("Item added");
        } catch (Exception e) { res.sendError(500, e.getMessage()); }
    }

 @Override
protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
    try {
        int id = Integer.parseInt(req.getParameter("id")); // ID comes from query string

        // Read body and parse parameters
        BufferedReader reader = req.getReader();
        String body = reader.lines().collect(Collectors.joining("&"));

        Map<String, String> params = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
            }
        }

        String name = params.get("name");
        double price = Double.parseDouble(params.get("price"));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            new ItemDAO(conn).update(new Item(id, name, price));
            res.getWriter().write("Item updated");
        }
    } catch (IOException | NumberFormatException | SQLException e) { // helpful for logs
        // helpful for logs
        res.sendError(500, "Server error: " + e.getMessage());
    }
}


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            new ItemDAO(conn).delete(id);
            res.getWriter().write("Item deleted");
        } catch (Exception e) { res.sendError(500, e.getMessage()); }
    }
}