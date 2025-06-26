package com.pahana.controller;

import com.pahana.dao.CustomerDAO;
import com.pahana.dao.ItemDAO;
import com.pahana.model.Customer;
import com.pahana.model.Item;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class BillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JSONObject body = new JSONObject(sb.toString());
        String accountNo = body.getString("accountNo");
        JSONArray itemsArray = body.getJSONArray("items");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {
            CustomerDAO cdao = new CustomerDAO(conn);
            ItemDAO idao = new ItemDAO(conn);
            Customer customer = cdao.find(accountNo);

            if (customer == null) {
                res.sendError(404, "Customer not found");
                return;
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

                // Save to DB
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

                // Add to response
                JSONObject savedItem = new JSONObject();
                savedItem.put("name", item.getName());
                savedItem.put("quantity", quantity);
                savedItem.put("subtotal", subtotal);
                savedItems.add(savedItem);
            }

            // Send response back
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("name", customer.getName());
            responseJSON.put("items", savedItems);
            responseJSON.put("total", total);

            res.setContentType("application/json");
            res.getWriter().write(responseJSON.toString());

        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }
    
   @Override
protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String summary = req.getParameter("summary");
    String history = req.getParameter("history");

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pahanaedu", "root", "")) {

        if ("true".equals(summary)) {
            Statement st = conn.createStatement();

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

            res.setContentType("application/json");
            res.getWriter().write(result.toString());
        }

        if ("true".equals(history)) {
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

            res.setContentType("application/json");
            res.getWriter().write(historyArray.toString());
        }

    } catch (Exception e) {
        res.sendError(500, "Server error: " + e.getMessage());
    }
}




}
