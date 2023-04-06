package org.delfin.service;

import org.delfin.exception.AccountNotFoundException;
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

    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public List<CustomerEntity> findAll() {
        return customerRepository.findAll();
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
        return new Customer(customerRepository.save(entity));
    }

    public Customer update(Customer customer) throws CustomerNotFoundException {
        CustomerEntity entity = findEntityById(customer.getId());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setUpdated(LocalDateTime.now());
        return new Customer(customerRepository.save(entity));
    }

    public void deleteById(Long id) {

    }

    public Account createAccountForCustomer(Long customerId, Account account) throws CustomerNotFoundException {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            AccountEntity entity = account.toEntity();
            entity.setCustomer(customer.get());
            return new Account(accountRepository.save(entity));
        } else {
            throw new CustomerNotFoundException(customerId);
        }
    }

    public List<Account> getAccountsForCustomer(Long customerId) throws CustomerNotFoundException, AccountNotFoundException {
        List<Account> accounts = new ArrayList<>();
        for (AccountEntity entity : accountRepository.findByCustomerId(customerId)) {
            accounts.add(new Account(entity));
        }
        return accounts;
    }
}
