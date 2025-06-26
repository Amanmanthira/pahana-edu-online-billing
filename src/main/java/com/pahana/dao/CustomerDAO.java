package com.pahana.dao;

import com.pahana.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO(Connection c) {
        this.conn = c;
    }

    public void add(Customer c) throws SQLException {
        String q = "INSERT INTO customers (account_no, name, address, phone, units) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, c.getAccountNo());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getPhone());
            ps.setInt(5, c.getUnits());
            ps.executeUpdate();
        }
    }

    public void update(Customer c) throws SQLException {
        String q = "UPDATE customers SET name=?, address=?, phone=?, units=? WHERE account_no=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getPhone());
            ps.setInt(4, c.getUnits());
            ps.setString(5, c.getAccountNo());
            ps.executeUpdate();
        }
    }

    public Customer find(String acc) throws SQLException {
        String q = "SELECT * FROM customers WHERE account_no=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, acc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getString("account_no"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getInt("units")
                    );
                }
            }
        }
        return null;
    }

    public List<Customer> findAll() throws SQLException {
        String q = "SELECT * FROM customers";
        List<Customer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(q);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer(
                    rs.getString("account_no"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getInt("units")
                );
                list.add(c);
            }
        }
        return list;
    }

    public boolean delete(String accountNo) throws SQLException {
        String q = "DELETE FROM customers WHERE account_no=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, accountNo);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
