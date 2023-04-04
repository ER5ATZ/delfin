package org.delfin.controller;

import org.delfin.model.Account;
import org.delfin.model.Customer;
import org.delfin.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody Customer customer) {
        LOG.info("Create customer: " + customer.toString());
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        LOG.info("Requested info for customerID " + id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        Optional<Customer> existingCustomer = customerService.findById(id);
        LOG.debug("Attempt to update customer with ID " + id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setFirstName(updatedCustomer.getFirstName());
            customer.setLastName(updatedCustomer.getLastName());
            customer.setUpdated(LocalDateTime.now());
            LOG.info("Updating customer " + customer);
            return ResponseEntity.ok(customerService.update(customer));
        } else {
            LOG.error("Customer not found for ID " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable Long id) {
        LOG.info("Requested accounts for customerID " + id);
        List<Account> accounts = customerService.getAccountsForCustomer(id);
        return ResponseEntity.ok(accounts);
    }
}

