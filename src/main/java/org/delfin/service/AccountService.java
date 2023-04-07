package org.delfin.service;

import org.delfin.exception.AccountExistsException;
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

import javax.security.auth.login.AccountException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

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
        if (id == null) throw new AccountNotFoundException(0L);
        return accountRepository.findActiveById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account save(Account account) throws AccountExistsException, CustomerNotFoundException {
        try {
            findEntityById(account.getId());
        } catch (AccountNotFoundException ex) {
            Long cid = Long.valueOf(account.getCustomer().toString());
            AccountEntity entity = account.toEntity();
            CustomerEntity customer = customerRepository
                    .findById(cid).orElseThrow((() -> new CustomerNotFoundException(cid)));
            entity.setCustomer(customer);
            entity.setActive(true);
            entity.setCreated(LocalDateTime.now());
            entity.setUpdated(entity.getCreated());
            return new Account(saveEntity(entity));
        }

        throw new AccountExistsException(account);
    }

    private AccountEntity saveEntity(AccountEntity entity) {
        return accountRepository.save(entity);
    }

    public Account update(Account account) throws AccountNotFoundException, CustomerNotFoundException {
        // TODO needs different degrees of rights on who may or may not update specific fields
        AccountEntity entity = findEntityById(account.getId());
        entity.setAccountLimit(account.getAccountLimit());
        Long cid = Long.valueOf(account.getCustomer().toString());
        CustomerEntity customer = customerRepository.findById(cid)
                .orElseThrow((() -> new CustomerNotFoundException(cid)));
        if (!customer.getId().equals(entity.getCustomer().getId())) {
            //Should customer be updated at all via API?
            //entity.setCustomer(customer);
            throw new CustomerNotFoundException(0L);
        }
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
