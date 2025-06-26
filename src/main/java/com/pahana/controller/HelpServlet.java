package com.pahana.controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;

public class HelpServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.getWriter().write("<h1>Help Section</h1><p>Use the menu to manage customers, items, and print bills. All actions require login.</p>");
    }
}