package com.rufino.server;

import java.util.List;

import com.rufino.server.model.Customer;
import com.rufino.server.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM customers");
	}

	/////////////////// GET ALL ORDERS/////////////////////////////////
	@Test
	void itShouldGetAllCustomers() {
		List<Customer> customersList = customerService.getAllCustomers();
		assertThat(customersList.size()).isEqualTo(0);

		Customer customer1 = new Customer();
		setCustomer(customer1, "regisrufino@gmail.com");
		saveAndAssert(customer1);

		Customer customer2 = new Customer();
		setCustomer(customer2, "regis2rufino@gmail.com");
		saveAndAssert(customer2, 1, 2);

		customersList = customerService.getAllCustomers();
		assertThat(customersList.size()).isEqualTo(2);
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
