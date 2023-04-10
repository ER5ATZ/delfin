package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.model.Currency;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    private CustomerEntity customer;
    @Column(nullable = false)
    private String currency = Currency.USD.code();
    @Column(nullable = false)
    private BigDecimal balance = new BigDecimal(0);
    @Column(name = "accountlimit", nullable = false)
    private BigDecimal accountLimit = new BigDecimal(0);
    @Column(nullable = false)
    private boolean active = true;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", customerId=" + getCustomer().getId() +
                ", currency='" + getCurrency() + '\'' +
                ", balance=" + getBalance() +
                ", accountLimit=" + getAccountLimit() +
                ", active=" + isActive() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                '}';
    }
}

