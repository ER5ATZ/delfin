package org.delfin.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String iban,
        String currency,
        BigDecimal balance,
        BigDecimal overdraftLimit,
        boolean active
) {
}
