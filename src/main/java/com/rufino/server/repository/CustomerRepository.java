package com.rufino.server.repository;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.CustomerDao;
import com.rufino.server.dao.JpaDao;
import com.rufino.server.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository implements CustomerDao {

    private JpaDao jpaDataAccess;

    @Autowired
    public CustomerRepository(JpaDao jpaDataAccess, JdbcTemplate jdbcTemplate) {
        this.jpaDataAccess = jpaDataAccess;
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        return jpaDataAccess.save(customer);
    }

    @Override
    public boolean deleteCustomer(UUID id) {
        try {
            jpaDataAccess.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Customer> getAll() {
        return jpaDataAccess.findAll();
    }

    @Override
    public Customer getCustomer(UUID id) {
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public Customer updateCustomer(UUID id, Customer customer) {
        customer.setCustomerId(id);
        return jpaDataAccess.save(customer);
    }
}