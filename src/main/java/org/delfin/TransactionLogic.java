package org.delfin;

import org.delfin.exception.TransactionExceedsLimitException;
import org.delfin.model.Transaction;
import org.delfin.model.CalculationType;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.service.TransactionTypeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* @author Andreas Ersch <andreas.ersch@gmail.com>
*/public class TransactionLogic {
    public static TransactionEntity verifyTransaction(
            BigDecimal accountLimit,
            BigDecimal balance,
            Transaction transaction
    ) throws TransactionExceedsLimitException {
        BigDecimal transactionAmount = transaction.getAmount();
        CalculationType transactionType = transaction.getTransactionType();

        BigDecimal previousBalance = balance;
        BigDecimal newBalance;

        if (transactionType == CalculationType.NEGATIVE
                && transactionAmount.abs().compareTo(accountLimit.add(balance)) > 0
        ) {
            throw new TransactionExceedsLimitException();
        }

        if (CalculationType.NEGATIVE.equals(transactionType)) {
            newBalance = balance.subtract(transactionAmount);
        } else {
            newBalance = balance.add(transactionAmount);
        }

        TransactionEntity result = new TransactionEntity();
        result.setExternalId(transaction.getExternalId());
        result.setPrevBalance(previousBalance);
        result.setNewBalance(newBalance);
        result.setAmount(transactionAmount);
        result.setBooked(true);
        result.setTransactionTypeId(
                TransactionTypeService.getTransactionTypeByCalculationType(transactionType.toString()).getId()
        );
        result.setTransactionTime(LocalDateTime.now());
        return result;
    }

}
