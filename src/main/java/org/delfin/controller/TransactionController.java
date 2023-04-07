package org.delfin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "TRANSACTION", description = "API for managing transactions")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "New transaction", description = "Creates a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Transaction> newTransaction(@RequestBody Transaction transaction) {
        try {
            LOG.info("Add transaction: " + transaction.toString());
            return ResponseEntity.ok(transactionService.createTransaction(transaction));
        } catch (AccountNotFoundException ex) {
            LOG.error("Failed to find account for transaction " + transaction);
            return ResponseEntity.notFound().build();
        } catch (TransactionExceedsLimitException e) {
            LOG.error("Transaction would have crossed limit " + transaction);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping(Endpoint.ID)
    @Operation(summary = "Get transaction by ID", description = "Returns data for a single transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
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
