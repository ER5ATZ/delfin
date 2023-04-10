package org.delfin.model.entity;

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
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid", nullable = false)
    private AccountEntity account;
    @Column(name = "externalid", nullable = false)
    private String externalId;
    @Column(name = "transactiontypeid", nullable = false)
    private Long transactionTypeId;
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(name = "prevbalance", nullable = false, precision = 10, scale = 2)
    private BigDecimal prevBalance;
    @Column(name = "newbalance", nullable = false, precision = 10, scale = 2)
    private BigDecimal newBalance;
    @Column(name = "transactiontime", nullable = false)
    private LocalDateTime transactionTime;
    @Column(name = "booked")
    private boolean booked;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", accountId=" + getAccount().getId() +
                ", externalId='" + getExternalId() + '\'' +
                ", transactionTypeId=" + getTransactionTypeId() +
                ", amount=" + getAmount() +
                ", prevBalance=" + getPrevBalance() +
                ", newBalance=" + getNewBalance() +
                ", transactiontime=" + getTransactionTime() +
                ", booked=" + isBooked() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                '}';
    }
}

