package com.pahana.service;

import com.pahana.dao.ItemDAO;
import com.pahana.model.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private final String DB_URL = "jdbc:mysql://localhost:3306/pahanaedu";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public List<Item> getAllItems() throws SQLException {
        try (Connection conn = getConnection()) {
            return new ItemDAO(conn).getAll();
        }
    }

    public void addItem(Item item) throws SQLException {
        try (Connection conn = getConnection()) {
            new ItemDAO(conn).add(item);
        }
    }

    public void updateItem(Item item) throws SQLException {
        try (Connection conn = getConnection()) {
            new ItemDAO(conn).update(item);
        }
    }

    public void deleteItem(int id) throws SQLException {
        try (Connection conn = getConnection()) {
            new ItemDAO(conn).delete(id);
        }
    }
}
