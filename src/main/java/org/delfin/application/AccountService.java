package org.delfin.application;

import org.delfin.domain.exception.AccountNotFoundException;
import org.delfin.domain.exception.CustomerNotFoundException;
import org.delfin.domain.model.Account;
import org.delfin.domain.model.Currency;
import org.delfin.domain.model.Iban;
import org.delfin.domain.model.Money;
import org.delfin.domain.repository.AccountRepository;
import org.delfin.domain.repository.CustomerRepository;
import org.delfin.domain.repository.LedgerEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public AccountService(AccountRepository accountRepository,
                         CustomerRepository customerRepository,
                         LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public Account createAccount(UUID customerId, Currency currency, Money overdraftLimit) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        Iban iban = Iban.generate("DE", "3704", randomDigits(10));
        Account account = new Account(customerId, iban, currency, overdraftLimit);
        return accountRepository.save(account);
    }

    public AccountWithBalance getAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        BigDecimal balance = accountRepository.sumBalance(accountId);
        return new AccountWithBalance(account, balance);
    }

    public Page<Object> getEntries(UUID accountId, Pageable pageable) {
        // Verify account exists
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        return ledgerEntryRepository.findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(entry -> (Object) entry);
    }

    public List<Account> findAccountsByCustomerId(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        return accountRepository.findAll().stream()
                .filter(acc -> acc.getCustomerId().equals(customerId))
                .toList();
    }

    private String randomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public static class AccountWithBalance {
        private final Account account;
        private final BigDecimal balance;

        public AccountWithBalance(Account account, BigDecimal balance) {
            this.account = account;
            this.balance = balance;
        }

        public Account getAccount() {
            return account;
        }

        public BigDecimal getBalance() {
            return balance;
        }
    }
}
