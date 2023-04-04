package org.delfin.service;

import org.delfin.model.Account;
import org.delfin.model.Customer;
import org.delfin.repository.AccountRepository;
import org.delfin.repository.CustomerRepository;
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
public class CustomerService {

    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }

    public Customer save(Customer customer) {
        customer.setCreated(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        customer.setUpdated(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Account> getAccountsForCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return accountRepository.findByCustomerId(customerId);
        } else {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
    }

    public Account createAccountForCustomer(Long customerId, Account account) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            account.setCustomerId(customer.get().getId());
            return accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
    }

    public Account updateAccountForCustomer(Long customerId, Long accountId, Account updatedAccount) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Optional<Account> account = accountRepository.findById(accountId);
            if (account.isPresent()) {
                account.get().setBalance(updatedAccount.getBalance());
                account.get().setUpdated(LocalDateTime.now());
                return accountRepository.save(account.get());
            } else {
                throw new EntityNotFoundException("Account not found with ID: " + accountId);
            }
        } else {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
    }
}
