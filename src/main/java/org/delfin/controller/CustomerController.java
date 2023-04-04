package org.delfin.controller;

import org.delfin.exception.CustomerNotFoundException;
import org.delfin.exception.AccountNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Customer;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.CustomerRepository;
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
@RequestMapping("/customers")
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
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        if(!updatedCustomer.getId().equals(id)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Customer customer = customerService.update(updatedCustomer);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable Long id) {
        List<Account> accounts = customerService.getAccountsForCustomer(id);
        return ResponseEntity.ok(accounts);
    }
}

