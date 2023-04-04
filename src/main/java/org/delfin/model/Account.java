package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "accountlimit", nullable = false)
    private BigDecimal accountLimit;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customer.getId() +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", accountLimit=" + accountLimit +
                ", active=" + active +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

