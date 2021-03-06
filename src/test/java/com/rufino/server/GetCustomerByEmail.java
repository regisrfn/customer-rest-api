package com.rufino.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCustomerByEmail {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM customers");
    }

    @Test
    void itShouldGetCustomerById() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");

        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/api/v1/customer/select?email=joe@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Is.is("Joe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerLastName", Is.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerPhone", Is.is("1111-2222")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail", Is.is("joe@gmail.com")))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void itShouldNotGetCustomerByEmail() throws Exception {
        JSONObject my_obj = new JSONObject();

        my_obj.put("customerName", "Joe");
        my_obj.put("customerLastName", "Doe");
        my_obj.put("customerPhone", "1111-2222");
        my_obj.put("customerEmail", "joe@gmail.com");

        mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/api/v1/customer/select?email=joe2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.apiError", Is.is("Customer not found")))
                .andExpect(status().isNotFound()).andReturn();

    }
}
