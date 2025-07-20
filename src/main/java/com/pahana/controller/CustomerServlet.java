package com.pahana.controller;
import com.pahana.model.Customer;
import com.pahana.service.CustomerService;
import com.pahana.util.DBConnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.List;

public class CustomerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Customer c = new Customer(
            req.getParameter("accountNo"), req.getParameter("name"),
            req.getParameter("address"), req.getParameter("phone"),
            Integer.parseInt(req.getParameter("units"))
        );

        try {
            Connection conn = DBConnection.getInstance();
            CustomerService service = new CustomerService(conn);
            service.addCustomer(c);
            res.getWriter().write("Customer added.");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader = req.getReader();
        String[] data = reader.readLine().split("&");
        String acc = data[0].split("=")[1];
        String name = data[1].split("=")[1];
        String addr = data[2].split("=")[1];
        String phone = data[3].split("=")[1];
        int units = Integer.parseInt(data[4].split("=")[1]);

        try {
            Connection conn = DBConnection.getInstance();
            CustomerService service = new CustomerService(conn);
            service.updateCustomer(new Customer(acc, name, addr, phone, units));
            res.getWriter().write("Customer updated.");
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String acc = req.getParameter("accountNo");
        if (acc == null || acc.isEmpty()) {
            res.sendError(400, "Missing accountNo");
            return;
        }
        try {
            Connection conn = DBConnection.getInstance();
            CustomerService service = new CustomerService(conn);
            boolean deleted = service.deleteCustomer(acc);
            if (deleted) {
                res.getWriter().write("Customer deleted.");
            } else {
                res.sendError(404, "Customer not found");
            }
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String acc = req.getParameter("accountNo");
        try {
            Connection conn = DBConnection.getInstance();
            CustomerService service = new CustomerService(conn);
            res.setContentType("application/json");

            if (acc == null || acc.isEmpty()) {
                List<Customer> customers = service.findAllCustomers();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < customers.size(); i++) {
                    Customer c = customers.get(i);
                    sb.append("{\"accountNo\":\"").append(c.getAccountNo())
                      .append("\",\"name\":\"").append(c.getName())
                      .append("\",\"address\":\"").append(c.getAddress())
                      .append("\",\"phone\":\"").append(c.getPhone())
                      .append("\",\"units\":").append(c.getUnits()).append("}");
                    if (i < customers.size() - 1) sb.append(",");
                }
                sb.append("]");
                res.getWriter().write(sb.toString());
            } else {
                Customer c = service.findCustomer(acc);
                if (c != null) {
                    res.getWriter().write("{\"accountNo\":\"" + c.getAccountNo() + "\",\"name\":\"" + c.getName() +
                            "\",\"address\":\"" + c.getAddress() + "\",\"phone\":\"" + c.getPhone() +
                            "\",\"units\":" + c.getUnits() + "}");
                } else {
                    res.sendError(404, "Not found");
                }
            }
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
        }
    }
}
