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
    private Object customer = 0L;
    private String currency = Currency.EUR.code();
    private BigDecimal balance = new BigDecimal(0);
    private BigDecimal accountLimit = new BigDecimal(0);
    private LocalDateTime created = LocalDateTime.MIN;

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
        customerEntity.setId((Long.valueOf(this.customer.toString())));
        accountEntity.setCustomer(customerEntity);
        accountEntity.setCurrency(this.currency);
        accountEntity.setBalance(this.balance);
        accountEntity.setAccountLimit(this.accountLimit);
        accountEntity.setCreated(this.created);
        return accountEntity;
    }
}




