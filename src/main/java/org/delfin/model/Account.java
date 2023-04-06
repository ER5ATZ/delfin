package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.constant.Endpoint;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.CustomerEntity;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    private Object customer;
    private String currency;
    private BigDecimal balance;
    private BigDecimal accountLimit;
    private LocalDateTime created;

    public Account(AccountEntity accountEntity) {
        this.id = accountEntity.getId();
        this.customer = Link.of(Endpoint.CUSTOMER + accountEntity.getCustomer().getId());
        this.currency = accountEntity.getCurrency();
        this.balance = accountEntity.getBalance();
        this.accountLimit = accountEntity.getAccountLimit();
        this.created = accountEntity.getCreated();
    }

    public AccountEntity toEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(this.id);
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId((Long) this.customer);
        accountEntity.setCustomer(customerEntity);
        accountEntity.setCurrency(this.currency);
        accountEntity.setBalance(this.balance);
        accountEntity.setAccountLimit(this.accountLimit);
        accountEntity.setCreated(this.created);
        return accountEntity;
    }
}




