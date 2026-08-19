package org.delfin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.delfin.api.dto.CreateEntryRequest;
import org.delfin.api.dto.LedgerEntryResponse;
import org.delfin.api.dto.TransferRequest;
import org.delfin.application.TransferService;
import org.delfin.domain.model.Currency;
import org.delfin.domain.model.EntryType;
import org.delfin.domain.model.LedgerEntry;
import org.delfin.domain.model.Money;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@Tag(name = "Transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/api/accounts/{id}/entries")
    @Operation(summary = "Create a ledger entry (deposit or withdrawal)")
    public ResponseEntity<LedgerEntryResponse> createEntry(
            @PathVariable UUID id,
            @Valid @RequestBody CreateEntryRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        EntryType type = EntryType.valueOf(request.type().toUpperCase());
        Currency currency = Currency.valueOf(request.currency().toUpperCase());
        Money amount = new Money(request.amount(), currency);

        LedgerEntry entry = transferService.createEntry(id, type, amount, request.description(), idempotencyKey);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/accounts/{accountId}/entries/{id}")
                .buildAndExpand(id, entry.getId())
                .toUri();

        LedgerEntryResponse response = new LedgerEntryResponse(
                entry.getId(),
                entry.getType().name(),
                entry.getAmount().getAmount(),
                entry.getAmount().getCurrency().getCode(),
                entry.getDescription(),
                entry.getIdempotencyKey(),
                entry.getCreatedAt()
        );

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/api/transfers")
    @Operation(summary = "Transfer funds from one account to another")
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        Currency currency = Currency.valueOf(request.currency().toUpperCase());
        Money amount = new Money(request.amount(), currency);

        transferService.transfer(
                request.sourceAccountId(),
                request.destinationAccountId(),
                amount,
                request.description(),
                idempotencyKey
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
