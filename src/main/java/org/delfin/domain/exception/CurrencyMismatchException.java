package org.delfin.domain.exception;

import org.delfin.domain.model.Currency;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException(Currency expected, Currency actual) {
        super(String.format("Currency mismatch: expected %s but got %s", expected.getCode(), actual.getCode()));
    }
}
