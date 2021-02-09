package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.CustomerDao;
import com.rufino.server.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer saveCustomer(Customer customer) {
        return customerDao.insertCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAll();
    }

    public Customer getCustomerById(UUID id) {
        return customerDao.getCustomer(id);
    }

    public int deleteCustomerById(UUID id) {
        return customerDao.deleteCustomer(id);
    }

    public Customer updateCustomer(UUID id, Customer customer) {
        return customerDao.updateCustomer(id, customer);
    }
}