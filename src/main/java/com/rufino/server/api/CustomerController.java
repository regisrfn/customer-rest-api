package com.rufino.server.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Customer;
import com.rufino.server.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
        try {
            UUID customerId = UUID.fromString(id);
            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null)
                throw new ApiRequestException("Customer not found", HttpStatus.NOT_FOUND);
            return customer;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteCustomerById(@PathVariable String id) {
        Map<String, String> message = new HashMap<>();

        try {
            UUID customerId = UUID.fromString(id);
            int op = customerService.deleteCustomerById(customerId);
            if (op == 0)
                throw new ApiRequestException("Customer not found", HttpStatus.NOT_FOUND);
            message.put("message", "successfully operation");
            return message;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Customer updateCustomer(@PathVariable String id, @Valid @RequestBody Customer customer, BindingResult bindingResult) {
        try {
            UUID customerId = UUID.fromString(id);
            Customer validatedCustomer = new Customer();

            if (hasErrorsCustomerRequest(customer, bindingResult, validatedCustomer))
                return customerService.updateCustomer(customerId, validatedCustomer);

            return customerService.updateCustomer(customerId, customer);

        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean hasErrorsCustomerRequest(Customer customer, BindingResult bindingResult, Customer validatedCustomer) {
        if (bindingResult.hasErrors()) {
            // ignore field password
            if (!bindingResult.hasFieldErrors("customerName")) {
                validatedCustomer.setCustomerName(customer.getCustomerName().toString());
            }
            if (!bindingResult.hasFieldErrors("customerLastName")) {
                validatedCustomer.setCustomerLastName(customer.getCustomerLastName());
            }
            if (!bindingResult.hasFieldErrors("customerPhone")) {
                validatedCustomer.setCustomerPhone(customer.getCustomerPhone());
            }
            if (!bindingResult.hasFieldErrors("customerEmail")) {
                validatedCustomer.setCustomerEmail(customer.getCustomerEmail());
            }
            if (!bindingResult.hasFieldErrors("customerCreatedAt")) {
                validatedCustomer.setCustomerCreatedAt(customer.getCustomerCreatedAt());
            }
            return true;
        }
        return false;
    }
}