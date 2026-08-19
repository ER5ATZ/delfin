package org.delfin.domain.exception;

import org.delfin.domain.model.Money;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Money requested, Money available, Money overdraftLimit) {
        super(String.format("Insufficient funds: requested %s, available %s (overdraft limit: %s)",
                requested.getAmount(), available.getAmount(), overdraftLimit.getAmount()));
    }
}
