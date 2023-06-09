package org.delfin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.delfin.constant.Endpoint;
import org.delfin.exception.AccountNotFoundException;
import org.delfin.exception.CustomerNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Customer;
import org.delfin.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping(Endpoint.CUSTOMER)
@Tag(name = "CUSTOMER", description = "API for managing customers")
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create customer", description = "Creates a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer created"),
            //@ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        LOG.info("Create customer: " + customer.toEntity().toString());
        try {
            return ResponseEntity.ok(customerService.save(customer));
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(Endpoint.ID)
    @Operation(summary = "Get customer by ID", description = "Returns data for a single customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        LOG.info("Requested info for customerID " + id);
        try {
            Customer customer = customerService.findById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(summary = "Update customer by ID", description = "Updates an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer) {
        LOG.debug("Attempt to update customer with ID " + updatedCustomer);
        try {
            return ResponseEntity.ok(customerService.update(updatedCustomer));
        } catch (CustomerNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(Endpoint.ACCOUNTS)
    @Operation(summary = "Get accounts for customer by ID", description = "Returns a list of accounts for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<List<Account>> getAccountsForCustomer(@PathVariable Long id) {
        LOG.info("Requested accounts for customerID " + id);
        try {
            List<Account> accounts = customerService.getAccountsForCustomer(id);
            return ResponseEntity.ok(accounts);
        } catch (CustomerNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
