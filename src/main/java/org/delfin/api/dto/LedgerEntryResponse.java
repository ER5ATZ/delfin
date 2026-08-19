package org.delfin.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID id,
        String type,
        BigDecimal amount,
        String currency,
        String description,
        String idempotencyKey,
        Instant createdAt
) {
}
