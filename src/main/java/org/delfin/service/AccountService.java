package org.delfin.service;

import org.delfin.exception.AccountNotFoundException;
import org.delfin.exception.CustomerNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Transaction;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.CustomerEntity;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.CustomerRepository;
import org.delfin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class AccountService {
    private AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account findById(Long id) throws AccountNotFoundException {
        return new Account(findEntityById(id));
    }

    private AccountEntity findEntityById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account save(Account account) {
        AccountEntity entity = account.toEntity();
        entity.setCreated(LocalDateTime.now());
        return new Account(saveEntity(entity));
    }

    private AccountEntity saveEntity(AccountEntity account) {
        return accountRepository.save(account);
    }

    public Account update(Account account) throws Exception {
        // TODO needs different degrees of rights on who may or may not updte what
        AccountEntity entity = findEntityById(account.getId());
        entity.setAccountLimit(account.getAccountLimit());
        Long cid = (Long) account.getCustomer();
        CustomerEntity customer = customerRepository.findById(cid).orElseThrow((() -> new CustomerNotFoundException(cid)));
        entity.setCustomer(customer);
        entity.setUpdated(LocalDateTime.now());
        return new Account(saveEntity(entity));
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) throws AccountNotFoundException {
        Optional<AccountEntity> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            List<Transaction> transactions = new ArrayList<>();
            // TODO use instead transactionRepository.findByAccountId(accountId, limit);
            for (TransactionEntity entity : transactionRepository.findByAccountId(accountId)) {
                transactions.add(new Transaction(entity));
            }
            return transactions;
        } else {
            throw new AccountNotFoundException(accountId);
        }
    }

    public void deleteById(Long id) {
        AccountEntity toDelete = findEntityById(id);
        toDelete.setActive(false);
        toDelete.setUpdated(LocalDateTime.now());
        saveEntity(toDelete);
    }
}
