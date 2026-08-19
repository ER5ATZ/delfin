package org.delfin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.delfin.api.dto.AccountResponse;
import org.delfin.api.dto.CreateAccountRequest;
import org.delfin.api.dto.LedgerEntryResponse;
import org.delfin.application.AccountService;
import org.delfin.domain.model.Account;
import org.delfin.domain.model.Currency;
import org.delfin.domain.model.LedgerEntry;
import org.delfin.domain.model.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Currency currency = Currency.valueOf(request.currency().toUpperCase());
        Money overdraftLimit = new Money(request.overdraftLimit(), currency);

        Account account = accountService.createAccount(request.customerId(), currency, overdraftLimit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getIban().getValue(),
                account.getCurrency().getCode(),
                request.overdraftLimit().negate(),
                request.overdraftLimit(),
                account.isActive()
        );
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account details with balance")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        AccountService.AccountWithBalance accountWithBalance = accountService.getAccount(id);
        Account account = accountWithBalance.getAccount();

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getIban().getValue(),
                account.getCurrency().getCode(),
                accountWithBalance.getBalance(),
                account.getOverdraftLimit().getAmount(),
                account.isActive()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/entries")
    @Operation(summary = "Get ledger entries for an account (paginated)")
    public ResponseEntity<Page<LedgerEntryResponse>> getEntries(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object> entries = accountService.getEntries(id, pageable);

        Page<LedgerEntryResponse> responsePage = entries.map(entry -> {
            LedgerEntry ledgerEntry = (LedgerEntry) entry;
            return new LedgerEntryResponse(
                    ledgerEntry.getId(),
                    ledgerEntry.getType().name(),
                    ledgerEntry.getAmount().getAmount(),
                    ledgerEntry.getAmount().getCurrency().getCode(),
                    ledgerEntry.getDescription(),
                    ledgerEntry.getIdempotencyKey(),
                    ledgerEntry.getCreatedAt()
            );
        });

        return ResponseEntity.ok(responsePage);
    }
}
