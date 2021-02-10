package com.rufino.server.dao;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Customer;

public interface CustomerDao {
    Customer insertCustomer(Customer customer);

    boolean deleteCustomer(UUID id);

    List<Customer> getAll();

    Customer getCustomer(UUID id);

    Customer getCustomerByEmail(String email);

    Customer updateCustomer(UUID id, Customer customer);
}