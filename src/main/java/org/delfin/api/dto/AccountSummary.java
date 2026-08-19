package org.delfin.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountSummary(
        UUID id,
        String iban,
        String currency,
        BigDecimal balance
) {
}
