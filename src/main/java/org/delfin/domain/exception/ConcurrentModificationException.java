package org.delfin.domain.exception;

import java.util.UUID;

public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException(UUID accountId) {
        super(String.format("Account %s was modified concurrently, retry the operation", accountId));
    }
}
