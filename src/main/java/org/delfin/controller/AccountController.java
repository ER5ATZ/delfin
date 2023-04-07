package org.delfin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.delfin.constant.Endpoint;
import org.delfin.exception.AccountExistsException;
import org.delfin.exception.AccountNotFoundException;
import org.delfin.exception.CustomerNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Transaction;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping(Endpoint.ACCOUNT)
@Tag(name = "ACCOUNT", description = "API for managing accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(summary = "Create account", description = "Creates a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        LOG.info("Create account: " + account.toString());
        try {
            return ResponseEntity.ok(accountService.save(account));
        } catch (AccountExistsException | CustomerNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(Endpoint.ID)
    @Operation(summary = "Get account by ID", description = "Returns data for a single account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        LOG.info("Requested info for accountID " + id);
        try {
            Account account = accountService.findById(id);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(summary = "Update account by ID", description = "Updates an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Account> updateAccount(@RequestBody Account updatedAccount) {
        LOG.debug("Attempt to update account with ID " + updatedAccount.getId());
        try {
            LOG.info("Updating account " + updatedAccount.toEntity().toString());
            return ResponseEntity.ok(accountService.update(updatedAccount));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(Endpoint.ID)
    @Operation(summary = "Get accounts for customer by ID", description = "Returns a list of accounts for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deactivated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        LOG.info("Deactivating account with ID" + id);
        try {
            accountService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException ex) {
            LOG.error("Account not found for ID " + id);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping(Endpoint.TRANSACTIONS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Long id) {
        LOG.info("Requested transaction for accountID " + id);
        try {
            List<Transaction> transactions = accountService.getTransactionsForAccount(id);
            return ResponseEntity.ok(transactions);
        } catch (AccountNotFoundException ex) {
            LOG.error("Account not found for ID " + id);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
