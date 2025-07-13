package com.pahana.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/teamchat")
public class MessageEndpoint {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("🔌 Connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session sender) throws IOException {
        System.out.println("📨 Message: " + message);
        for (Session session : sessions) {
            if (session.isOpen() && !session.getId().equals(sender.getId())) {
                session.getBasicRemote().sendText(message);
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
