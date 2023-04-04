package org.delfin.service;

import org.delfin.exception.AccountNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Transaction;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class AccountService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id)));
    }

    public Account save(Account account) {
        account.setCreated(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Account update(Account account) {
        account.setUpdated(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            return transactionRepository.findByAccountId(accountId);
        } else {
            throw new AccountNotFoundException(accountId);
        }
    }
}
