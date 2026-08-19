package org.delfin.domain.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID customerId) {
        super(String.format("Customer not found: %s", customerId));
    }
}
