package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.constant.Endpoint;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.TransactionEntity;
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
public class Transaction {

    private Long id;
    private Object account;
    private String externalId;
    private Long transactionTypeId;
    private BigDecimal amount;
    private BigDecimal prevBalance;
    private BigDecimal newBalance;
    private LocalDateTime transactiontime;
    private Boolean booked;

    public Transaction(TransactionEntity transactionEntity) {
        this.id = transactionEntity.getId();
        this.account = Link.of(Endpoint.ACCOUNT + transactionEntity.getAccount().getId());
        this.externalId = transactionEntity.getExternalId();
        this.transactionTypeId = transactionEntity.getTransactionTypeId();
        this.amount = transactionEntity.getAmount();
        this.prevBalance = transactionEntity.getPrevBalance();
        this.newBalance = transactionEntity.getNewBalance();
        this.transactiontime = transactionEntity.getTransactiontime();
        this.booked = transactionEntity.getBooked();
    }

    public TransactionEntity toEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(this.id);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId((Long) this.account);
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setExternalId(this.externalId);
        transactionEntity.setTransactionTypeId(this.transactionTypeId);
        transactionEntity.setAmount(this.amount);
        transactionEntity.setPrevBalance(this.prevBalance);
        transactionEntity.setNewBalance(this.newBalance);
        transactionEntity.setTransactiontime(this.transactiontime);
        transactionEntity.setBooked(this.booked);
        return transactionEntity;
    }
}

