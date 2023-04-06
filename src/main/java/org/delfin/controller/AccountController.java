package org.delfin.controller;

import org.delfin.constant.Endpoint;
import org.delfin.exception.AccountNotFoundException;
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
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        LOG.info("Create account: " + account.toString());
        try {
            return ResponseEntity.ok(accountService.save(account));
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
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

    @PutMapping("{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account updatedAccount) {
        LOG.debug("Attempt to update account with ID " + id);
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

    @DeleteMapping("{id}")
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

    @GetMapping("{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Long id) {
        LOG.info("Requested transaction for accountID " + id);
        List<Transaction> transactions = accountService.getTransactionsForAccount(id);
        return ResponseEntity.ok(transactions);
    }
}
