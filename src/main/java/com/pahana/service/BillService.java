package com.pahana.service;
import com.pahana.dao.CustomerDAO;
import com.pahana.dao.ItemDAO;
import com.pahana.model.Customer;
import com.pahana.model.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillService {

    private final String DB_URL = "jdbc:mysql://localhost:3306/pahanaedu";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Process a bill with customer accountNo and a JSONArray of items [{itemId, quantity}, ...]
    public JSONObject processBill(String accountNo, JSONArray itemsArray) throws SQLException {
        try (Connection conn = getConnection()) {
            CustomerDAO cdao = new CustomerDAO(conn);
            ItemDAO idao = new ItemDAO(conn);

            Customer customer = cdao.find(accountNo);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found");
            }

            double total = 0.0;
            List<JSONObject> savedItems = new ArrayList<>();

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObj = itemsArray.getJSONObject(i);
                int itemId = itemObj.getInt("itemId");
                int quantity = itemObj.getInt("quantity");

                Item item = idao.find(itemId);
                if (item == null) continue;

                double unitPrice = item.getPrice();
                double subtotal = unitPrice * quantity;
                total += subtotal;

                // Save bill entry to DB
                String insertSQL = "INSERT INTO tdb (account_no, item_id, quantity, unit_price, subtotal, total) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    ps.setString(1, accountNo);
                    ps.setInt(2, itemId);
                    ps.setInt(3, quantity);
                    ps.setDouble(4, unitPrice);
                    ps.setDouble(5, subtotal);
                    ps.setDouble(6, total);
                    ps.executeUpdate();
                }

                JSONObject savedItem = new JSONObject();
                savedItem.put("name", item.getName());
                savedItem.put("quantity", quantity);
                savedItem.put("subtotal", subtotal);
                savedItems.add(savedItem);
            }

            JSONObject responseJSON = new JSONObject();
            responseJSON.put("name", customer.getName());
            responseJSON.put("items", savedItems);
            responseJSON.put("total", total);

            return responseJSON;
        }
    }

    // Get summary info: total customers, items, billing amount
    public JSONObject getSummary() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM customers");
            rs1.next();
            int totalCustomers = rs1.getInt(1);

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM items");
            rs2.next();
            int totalItems = rs2.getInt(1);

            ResultSet rs3 = st.executeQuery("SELECT SUM(total) FROM tdb");
            rs3.next();
            double totalBillingAmount = rs3.getDouble(1);

            JSONObject result = new JSONObject();
            result.put("totalCustomers", totalCustomers);
            result.put("totalItems", totalItems);
            result.put("totalBills", totalBillingAmount);

            return result;
        }
    }

    // Get billing history as JSONArray
    public JSONArray getHistory() throws SQLException {
        try (Connection conn = getConnection()) {
            JSONArray historyArray = new JSONArray();

            String sql = "SELECT account_no, total, created_at FROM tdb ORDER BY created_at DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("accountNo", rs.getString("account_no"));
                    obj.put("total", rs.getDouble("total"));
                    obj.put("date", rs.getTimestamp("created_at").toString());
                    historyArray.put(obj);
                }
            }
            return historyArray;
        }
    }
}
