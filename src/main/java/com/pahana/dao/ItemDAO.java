package com.pahana.dao;

import com.pahana.model.Item;
import java.sql.*;
import java.util.*;

public class ItemDAO {
    private final Connection conn;
    public ItemDAO(Connection c) { conn = c; }

    public void add(Item i) throws SQLException {
        String q = "INSERT INTO items (name, price_per_unit, quantity) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, i.getName());
        ps.setDouble(2, i.getPrice());
        ps.setInt(3, i.getQuantity());
        ps.executeUpdate();
    }

    public void update(Item i) throws SQLException {
        String q = "UPDATE items SET name=?, price_per_unit=?, quantity=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, i.getName());
        ps.setDouble(2, i.getPrice());
        ps.setInt(3, i.getQuantity());
        ps.setInt(4, i.getId());
        ps.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM items WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public List<Item> getAll() throws SQLException {
        List<Item> list = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM items");
        while (rs.next()) {
            list.add(new Item(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price_per_unit"),
                rs.getInt("quantity")
            ));
        }
        return list;
    }

    public Item find(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM items WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Item(
                id,
                rs.getString("name"),
                rs.getDouble("price_per_unit"),
                rs.getInt("quantity")
            );
        }
        return null;
    }
}
