package org.delfin.service;

import org.delfin.TransactionLogic;
import org.delfin.exception.AccountNotFoundException;
import org.delfin.exception.TransactionExceedsLimitException;
import org.delfin.exception.TransactionNotFoundException;
import org.delfin.model.Transaction;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Transaction createTransaction(Transaction transaction) throws AccountNotFoundException, TransactionExceedsLimitException {
        Long accountId = (Long) transaction.getAccount();
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(()-> new AccountNotFoundException(accountId));
        TransactionEntity entity = TransactionLogic.calculateNewBalance(
                account.getAccountLimit(),
                account.getBalance(),
                transaction
        );

        transactionRepository.save(entity);
        account.setBalance(entity.getNewBalance());
        accountRepository.save(account);
        return new Transaction(entity);
    }

    public List<TransactionEntity> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction findById(Long id) throws TransactionNotFoundException {
        return new Transaction(transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id)));
    }

    public TransactionEntity save(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }
}