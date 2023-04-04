package org.delfin.service;

import org.delfin.exception.AccountNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Transaction;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Transaction transaction) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(transaction.getAccountId());
        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException(transaction.getAccountId());
        }
        Account account = optionalAccount.get();

        transaction.setAccountId(account.getId());
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public BigDecimal calculateAccountBalance(Long accountId) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException(accountId);
        }
        Account account = optionalAccount.get();

        BigDecimal balance = account.getBalance();
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        for (Transaction transaction : transactions) {
            balance = balance.add(transaction.getAmount());
        }

        return balance;
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}