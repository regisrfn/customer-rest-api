package com.rufino.server;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.core.Is;

@SpringBootTest
@AutoConfigureMockMvc
public class PostRequestTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM customers");
    }

    @Test
    void itShouldSaveCustomer() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");
        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Is.is("Joe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerLastName", Is.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerPhone", Is.is("1111-2222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail", Is.is("joe@gmail.com")))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void itShouldNotSaveCustomer() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");
        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors.customerLastName", Is.is("Value should not be empty")))
                .andExpect(status().isBadRequest()).andReturn();

        my_obj = new JSONObject();
        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.customerName", Is.is("Value should not be empty")))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors.customerLastName", Is.is("Value should not be empty")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.customerPhone", Is.is("Value should not be empty")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.customerEmail", Is.is("Value should not be empty")))
                .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void itShouldNotSaveCustomer_emailAlreadyExists() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");

        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors.customerEmail", Is.is("Email not available")))
                .andExpect(status().isBadRequest()).andReturn();

    }
}
