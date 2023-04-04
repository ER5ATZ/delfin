package org.delfin.controller;

import org.delfin.model.Account;
import org.delfin.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        LOG.info("Create account: " + account.toString());
        return accountService.save(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.findById(id);
        LOG.info("Requested info for accountID " + id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account updatedAccount) {
        Optional<Account> existingAccount = accountService.findById(id);
        LOG.debug("Attempt to update account with ID " + id);

        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();
            account.setCurrency(updatedAccount.getCurrency());
            account.setBalance(updatedAccount.getBalance());
            account.setAccountLimit(updatedAccount.getAccountLimit());
            account.setUpdated(LocalDateTime.now());
            LOG.info("Updating account " + account.toString());
            return ResponseEntity.ok(accountService.update(account));
        } else {
            LOG.error("Account not found for ID " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.findById(id);
        if (account.isPresent()) {
            Account deleted = account.get();
            deleted.setActive(false);
            accountService.update(deleted);
            LOG.info("Deactivating account with ID" + id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.error("Account not found for ID " + id);
            return ResponseEntity.notFound().build();
        }
    }
}
