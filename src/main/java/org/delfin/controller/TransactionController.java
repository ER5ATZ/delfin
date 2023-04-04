package org.delfin.controller;

import org.delfin.exception.AccountNotFoundException;
import org.delfin.model.Transaction;
import org.delfin.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        try {
            return ResponseEntity.ok(transactionService.createTransaction(transaction));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);

        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* If we add two steps to transactions, one for receiving, another for booking,
     * it would make sense to update the state of the transaction, for simplicity we
     * should skip that for now
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction updatedTransaction
    ) {
        Optional<Transaction> existingTransaction = transactionService.findById(id);

        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            transaction.setAccountId(updatedTransaction.getAccountId());
            transaction.setTransactionTypeId(updatedTransaction.getTransactionTypeId());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setBooked(updatedTransaction.getBooked());
            return ResponseEntity.ok(transactionService.save(transaction));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     */

    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.getTransactionsForAccount(id);
        return ResponseEntity.ok(transactions);
    }
}
