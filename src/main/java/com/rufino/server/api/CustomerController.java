package com.rufino.server.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Customer;
import com.rufino.server.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customer")
@CrossOrigin
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteCustomerById(@PathVariable String id) {
        customerService.deleteCustomerById(id);
        return Map.of("message", "successfully operation");
    }

    @PutMapping("{id}")
    public Customer updateCustomer(@PathVariable String id, @Valid @RequestBody Customer customer) {
        try {
            UUID customerId = UUID.fromString(id);
            return customerService.updateCustomer(customerId, customer);

        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }

}