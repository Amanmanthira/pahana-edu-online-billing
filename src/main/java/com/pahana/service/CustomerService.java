package com.pahana.service;

import com.pahana.dao.CustomerDAO;
import com.pahana.model.Customer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private CustomerDAO dao;

    public CustomerService(Connection conn) {
        dao = new CustomerDAO(conn);
    }

    public void addCustomer(Customer c) throws SQLException {
        dao.add(c);
    }

    public void updateCustomer(Customer c) throws SQLException {
        dao.update(c);
    }

    public Customer findCustomer(String accountNo) throws SQLException {
        return dao.find(accountNo);
    }

    // New: get all customers
    public List<Customer> findAllCustomers() throws SQLException {
        // Add findAll() in DAO too
        return dao.findAll();
    }

    public boolean deleteCustomer(String accountNo) throws SQLException {
        return dao.delete(accountNo);
    }
}
