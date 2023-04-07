package org.delfin.service;

import org.delfin.exception.CustomerNotFoundException;
import org.delfin.model.Account;
import org.delfin.model.Customer;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.CustomerEntity;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.CustomerRepository;
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
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public Customer findById(Long id) throws CustomerNotFoundException {
        return new Customer(findEntityById(id));
    }

    private CustomerEntity findEntityById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer save(Customer customer) throws RuntimeException {
        CustomerEntity entity = customer.toEntity();
        entity.setCreated(LocalDateTime.now());
        entity.setUpdated(entity.getCreated());
        return new Customer(customerRepository.save(entity));
    }

    public Customer update(Customer customer) throws CustomerNotFoundException {
        CustomerEntity entity = findEntityById(customer.getId());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setActive(customer.isActive());
        entity.setUpdated(LocalDateTime.now());
        return new Customer(customerRepository.save(entity));
    }

    public Account createAccountForCustomer(Long customerId, Account account) throws CustomerNotFoundException {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException(customerId);
        }
        AccountEntity entity = account.toEntity();
        entity.setCustomer(customer.get());
        return new Account(accountRepository.save(entity));
    }

    public List<Account> getAccountsForCustomer(Long customerId) throws CustomerNotFoundException {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException(customerId);
        }
        List<Account> accounts = new ArrayList<>();
        for (AccountEntity entity : accountRepository.findByCustomerId(customerId)) {
            accounts.add(new Account(entity));
        }
        return accounts;
    }
}
