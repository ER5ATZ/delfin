package org.delfin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEntryRequest(
        @NotBlank String type,
        @NotNull BigDecimal amount,
        @NotBlank String currency,
        String description
) {
}
