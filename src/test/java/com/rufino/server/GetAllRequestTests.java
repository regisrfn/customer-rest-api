package com.rufino.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.Customer;
import com.rufino.server.service.CustomerService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class GetAllRequestTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM CUSTOMERS");
    }

    @Test
    void itShouldGetAllCustomers() throws Exception {
        JSONObject my_obj = new JSONObject();

        MvcResult result = mockMvc
                .perform(get("/api/v1/customer/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        List<Customer> customerList = Arrays
                .asList(objectMapper.readValue(result.getResponse().getContentAsString(), Customer[].class));

        assertThat(customerList.size()).isEqualTo(0);

        Customer customer1 = new Customer();
		setCustomer(customer1, "regisrufino@gmail.com");
		saveAndAssert(customer1);

		Customer customer2 = new Customer();
		setCustomer(customer2, "regis2rufino@gmail.com");
        saveAndAssert(customer2, 1, 2);
        
        result = mockMvc
                .perform(get("/api/v1/customer/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        customerList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), Customer[].class));

        assertThat(customerList.size()).isEqualTo(2);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    private void saveAndAssert(Customer customer) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from customers", Long.class);
        assertEquals(0, countBeforeInsert);
        customerService.saveCustomer(customer);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from customers", Long.class);
        assertEquals(1, countAfterInsert);
    }

    private void saveAndAssert(Customer customer, int before, int after) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from customers", Long.class);
        assertEquals(before, countBeforeInsert);
        customerService.saveCustomer(customer);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from customers", Long.class);
        assertEquals(after, countAfterInsert);
    }

    private void setCustomer(Customer customer, String customerEmail) {
        customer.setCustomerEmail(customerEmail);
        customer.setCustomerLastName("Rufino");
        customer.setCustomerName("Regis");
        customer.setCustomerPhone("3317-1320");
    }

}
