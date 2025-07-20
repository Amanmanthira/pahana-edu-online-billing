package com.pahana.controller;

import com.pahana.model.Item;
import com.pahana.service.ItemService;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class ItemServlet extends HttpServlet {

    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        try (PrintWriter out = res.getWriter()) {
            List<Item> items = itemService.getAllItems();
            out.print("[");
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                out.print(String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"price\":%.2f,\"quantity\":%d}",
                    item.getId(), item.getName(), item.getPrice(), item.getQuantity()
                ));
                if (i < items.size() - 1) out.print(",");
            }
            out.print("]");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            itemService.addItem(new Item(0, name, price, quantity));
            res.getWriter().write("Item added");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));

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
            int quantity = Integer.parseInt(params.get("quantity"));

            itemService.updateItem(new Item(id, name, price, quantity));
            res.getWriter().write("Item updated");

        } catch (Exception e) {
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            itemService.deleteItem(id);
            res.getWriter().write("Item deleted");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }
}
