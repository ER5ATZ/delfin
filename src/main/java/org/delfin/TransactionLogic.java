package org.delfin;

import org.delfin.exception.TransactionExceedsLimitException;
import org.delfin.model.Transaction;
import org.delfin.model.entity.TransactionEntity;

import java.math.BigDecimal;

/**
* @author Andreas Ersch <andreas.ersch@gmail.com>
*/public class TransactionLogic {
    public static TransactionEntity calculateNewBalance(
            BigDecimal accountLimit,
            BigDecimal balance,
            Transaction transaction) throws TransactionExceedsLimitException {
        // TODO calculate values
        return new TransactionEntity();
    }
}
