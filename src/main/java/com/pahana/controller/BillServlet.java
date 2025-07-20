package com.pahana.controller;

import com.pahana.service.BillService;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;

public class BillServlet extends HttpServlet {

    private final BillService billService = new BillService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject body = new JSONObject(sb.toString());
            String accountNo = body.getString("accountNo");
            JSONArray itemsArray = body.getJSONArray("items");

            JSONObject responseJSON = billService.processBill(accountNo, itemsArray);

            res.setContentType("application/json");
            res.getWriter().write(responseJSON.toString());

        } catch (IllegalArgumentException e) {
            res.sendError(404, e.getMessage());
        } catch (Exception e) {
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String summary = req.getParameter("summary");
        String history = req.getParameter("history");

        try {
            if ("true".equals(summary)) {
                JSONObject result = billService.getSummary();
                res.setContentType("application/json");
                res.getWriter().write(result.toString());
            } else if ("true".equals(history)) {
                JSONArray historyArray = billService.getHistory();
                res.setContentType("application/json");
                res.getWriter().write(historyArray.toString());
            } else {
                res.sendError(400, "Invalid request parameters");
            }
        } catch (Exception e) {
            res.sendError(500, "Server error: " + e.getMessage());
        }
    }
}
