package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.CustomerDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public Customer getCustomerById(String id) {
        try {
            UUID customerId = UUID.fromString(id);
            Customer customer = customerDao.getCustomer(customerId);
            if (customer == null)
                throw new ApiRequestException("Customer not found", HttpStatus.NOT_FOUND);
            return customer;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid customer UUID format", HttpStatus.BAD_REQUEST);
        }

    }

    public boolean deleteCustomerById(String id) {
        try {
            UUID customerId = UUID.fromString(id);
            boolean ok = customerDao.deleteCustomer(customerId);
            if (!ok)
                throw new ApiRequestException("Customer not found", HttpStatus.NOT_FOUND);
            return ok;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid customer UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    public Customer updateCustomer(String id, Customer customer) {
        try {
            UUID customerId = UUID.fromString(id);
            return customerDao.updateCustomer(customerId, customer);
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid customer UUID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerDao.getCustomerByEmail(email);
        if (customer == null)
            throw new ApiRequestException("Customer not found", HttpStatus.NOT_FOUND);
        return customer;
    }
}