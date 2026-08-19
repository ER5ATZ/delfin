package org.delfin.api.dto;

import java.util.List;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        List<AccountSummary> accounts
) {
}
