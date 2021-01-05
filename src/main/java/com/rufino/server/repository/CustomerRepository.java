package com.rufino.server.repository;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.dao.JpaDao;
import com.rufino.server.dao.CustomerDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository implements CustomerDao {

    private JpaDao jpaDataAccess;
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper om;

    @Autowired
    public CustomerRepository(JpaDao jpaDataAccess, JdbcTemplate jdbcTemplate) {
        this.jpaDataAccess = jpaDataAccess;
        this.jdbcTemplate = jdbcTemplate;
        this.om = new ObjectMapper();

    }

    @Override
    public Customer insertCustomer(Customer customer) {
        return jpaDataAccess.save(customer);
    }

    @Override
    public int deleteCustomer(UUID id) {
        try {
            jpaDataAccess.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
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
        String customerString;

        try {
            customerString = om.writeValueAsString(customer);
            String sql = generateSqlUpdate(customer, customerString);
            int result = jdbcTemplate.update(sql + "where customer_id = ?", id);
            return (result > 0 ? getCustomer(id) : null);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage());
        }
    }

    private String generateSqlUpdate(Customer customer, String customerString) throws JSONException {
        String sql = "UPDATE CUSTOMERS SET ";
        JSONObject jsonObject = new JSONObject(customerString);
        Iterator<String> keys = jsonObject.keys();
        if (!keys.hasNext()) {
            throw new ApiRequestException("No valid data to update");
        }
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("customerCreatedAt"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='"
                        + customer.getCustomerCreatedAt().toString() + "' ";
            else
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + jsonObject.get(key) + "' ";

            if (keys.hasNext()) {
                sql = sql + ", ";
            }
        }
        return sql;
    }
}