package org.delfin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.delfin.api.dto.AccountSummary;
import org.delfin.api.dto.CreateCustomerRequest;
import org.delfin.api.dto.CustomerResponse;
import org.delfin.application.CustomerService;
import org.delfin.domain.model.Account;
import org.delfin.domain.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(request.firstName(), request.lastName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();

        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                java.util.List.of()
        );
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer details with their accounts")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        CustomerService.CustomerWithAccounts customerWithAccounts = customerService.getCustomer(id);
        Customer customer = customerWithAccounts.getCustomer();

        java.util.List<AccountSummary> accountSummaries = customerWithAccounts.getAccounts().stream()
                .map(account -> {
                    BigDecimal balance = BigDecimal.ZERO; // Will be calculated per account via AccountService
                    return new AccountSummary(
                            account.getId(),
                            account.getIban().getValue(),
                            account.getCurrency().getCode(),
                            balance
                    );
                })
                .toList();

        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                accountSummaries
        );
        return ResponseEntity.ok(response);
    }
}
