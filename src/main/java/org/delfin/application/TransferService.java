package org.delfin.application;

import org.delfin.domain.exception.AccountNotFoundException;
import org.delfin.domain.exception.ConcurrentModificationException;
import org.delfin.domain.exception.CurrencyMismatchException;
import org.delfin.domain.model.Account;
import org.delfin.domain.model.EntryType;
import org.delfin.domain.model.LedgerEntry;
import org.delfin.domain.model.Money;
import org.delfin.domain.repository.AccountRepository;
import org.delfin.domain.repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class TransferService {
    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public TransferService(AccountRepository accountRepository,
                          LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public LedgerEntry createEntry(UUID accountId, EntryType type, Money amount, String description, String idempotencyKey) {
        // Check idempotency - if entry with same key exists, return it
        var existingEntry = ledgerEntryRepository.findByAccountIdAndIdempotencyKey(accountId, idempotencyKey);
        if (existingEntry.isPresent()) {
            return existingEntry.get();
        }

        // Verify account exists
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        // For DEBIT: validate overdraft
        if (type == EntryType.DEBIT) {
            BigDecimal currentBalance = accountRepository.sumBalance(accountId);
            Money balance = new Money(currentBalance, account.getCurrency());
            account.validateDebit(amount, balance);
        }

        // Create and save entry
        LedgerEntry entry = new LedgerEntry(accountId, type, amount, description, idempotencyKey);
        return ledgerEntryRepository.save(entry);
    }

    public void transfer(UUID sourceAccountId, UUID destinationAccountId, Money amount, String description, String idempotencyKey) {
        // Fetch both accounts
        Account source = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));
        Account destination = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new AccountNotFoundException(destinationAccountId));

        // Validate currencies match
        if (!source.getCurrency().equals(destination.getCurrency())) {
            throw new CurrencyMismatchException(source.getCurrency(), destination.getCurrency());
        }

        try {
            // Get current balance and validate overdraft
            BigDecimal currentBalance = accountRepository.sumBalance(sourceAccountId);
            Money balance = new Money(currentBalance, source.getCurrency());
            source.validateDebit(amount, balance);

            // Trigger optimistic lock by saving source
            accountRepository.save(source);

            // Create DEBIT on source
            String debitKey = idempotencyKey + "-debit";
            createEntry(sourceAccountId, EntryType.DEBIT, amount, description, debitKey);

            // Create CREDIT on destination
            String creditKey = idempotencyKey + "-credit";
            createEntry(destinationAccountId, EntryType.CREDIT, amount, description, creditKey);
        } catch (jakarta.persistence.OptimisticLockException ex) {
            throw new ConcurrentModificationException(sourceAccountId);
        }
    }
}
