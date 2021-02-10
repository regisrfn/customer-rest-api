package com.rufino.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rufino.server.model.Customer;

import org.hamcrest.core.Is;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PutRequestsTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM customers");
    }

    @Test
    void itShouldUpdateCustomerById() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");

        MvcResult result = mockMvc
                .perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        Customer customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);

        my_obj.put("customerName", "John");
        mockMvc.perform(put("/api/v1/customer/" + customerResponse.getCustomerId())
                .contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Is.is("John"))).andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void itShouldNotUpdateCustomerById() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");

        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(put("/api/v1/customer/1").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.apiError", Is.is("Invalid customer UUID format")))
                .andExpect(status().isBadRequest()).andReturn();

    }
}
