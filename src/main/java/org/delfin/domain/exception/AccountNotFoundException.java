package org.delfin.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID accountId) {
        super(String.format("Account not found: %s", accountId));
    }
}
