package com.pahana.controller;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/teamchat")
public class MessageEndpoint {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    
    private static final String URL = "jdbc:mysql://localhost:3306/pahanaedu";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessions.add(session);
        System.out.println("🔌 Connected: " + session.getId());

        // Send previous chat messages
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, role, message, timestamp FROM team_chat ORDER BY id ASC")) {
            while (rs.next()) {
                String formatted = "<strong>" + rs.getString("username") + " (" + rs.getString("role") + ")</strong>: "
                        + rs.getString("message") + " <small style='color:gray;'>[" + rs.getTimestamp("timestamp") + "]</small>";
                session.getBasicRemote().sendText(formatted);
            }
        } catch (SQLException e) {
            System.err.println("❌ DB Read Error: " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String rawMessage, Session sender) throws IOException {
        System.out.println("📨 Received: " + rawMessage);

        // Sanitize and extract info
        String cleanMsg = rawMessage.replaceAll("<[^>]*>", "");
        String[] parts = cleanMsg.split(":", 2);
        if (parts.length < 2) return;

        String userPart = parts[0].trim();
        String messageText = parts[1].trim();

        String username = userPart.contains("(") ? userPart.substring(0, userPart.indexOf("(")).trim() : "Unknown";
        String role = userPart.contains("(") ? userPart.substring(userPart.indexOf("(") + 1, userPart.indexOf(")")).trim() : "Guest";

        // Save to DB directly here
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO team_chat (username, role, message) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, role);
            stmt.setString(3, messageText);
            int rows = stmt.executeUpdate();
            System.out.println("✅ Chat saved to DB, rows affected: " + rows);
        } catch (SQLException e) {
            System.err.println("❌ DB Save Error: " + e.getMessage());
        }

        // Broadcast message to all connected clients
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(rawMessage);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("❌ Disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("⚠️ Error: " + throwable.getMessage());
    }
}