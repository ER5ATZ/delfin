package org.delfin.application;

import org.delfin.domain.exception.CustomerNotFoundException;
import org.delfin.domain.model.Account;
import org.delfin.domain.model.Customer;
import org.delfin.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    public CustomerService(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    public Customer createCustomer(String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName);
        return customerRepository.save(customer);
    }

    public CustomerWithAccounts getCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Account> accounts = accountService.findAccountsByCustomerId(customerId);
        return new CustomerWithAccounts(customer, accounts);
    }

    public static class CustomerWithAccounts {
        private final Customer customer;
        private final List<Account> accounts;

        public CustomerWithAccounts(Customer customer, List<Account> accounts) {
            this.customer = customer;
            this.accounts = accounts;
        }

        public Customer getCustomer() {
            return customer;
        }

        public List<Account> getAccounts() {
            return accounts;
        }
    }
}
