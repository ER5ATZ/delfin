package org.delfin.controller;

import org.delfin.constant.Endpoint;
import org.delfin.exception.AccountNotFoundException;
import org.delfin.exception.TransactionExceedsLimitException;
import org.delfin.exception.TransactionNotFoundException;
import org.delfin.model.Transaction;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@RestController
@RequestMapping(Endpoint.TRANSACTION)
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
            LOG.info("Add transaction: " + transaction.toString());
            return ResponseEntity.ok(transactionService.createTransaction(transaction));
        } catch (AccountNotFoundException ex) {
            LOG.error("Failed to add transaction " + transaction);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (TransactionExceedsLimitException e) {
            LOG.error("Transaction would have crossed limit " + transaction);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        LOG.info("Requested info for transactionID " + id);
        try {
            Transaction transaction = transactionService.findById(id);
            return ResponseEntity.ok(transaction);
        } catch (TransactionNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            LOG.error("Unexpected internal error: " + ex);
            return ResponseEntity.internalServerError().build();
        }

    }
}
