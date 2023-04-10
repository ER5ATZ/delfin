package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.constant.Endpoint;
import org.delfin.model.entity.AccountEntity;
import org.delfin.model.entity.TransactionEntity;
import org.delfin.model.entity.TransactionTypeEntity;
import org.delfin.service.TransactionTypeService;
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
    private CalculationType transactionType;
    private BigDecimal amount;
    private BigDecimal prevBalance;
    private BigDecimal newBalance;
    private LocalDateTime transactionTime;
    private boolean booked;

    public Transaction(TransactionEntity transactionEntity) {
        this.id = transactionEntity.getId();
        this.account = Link.of(Endpoint.ACCOUNT + transactionEntity.getAccount().getId());
        this.externalId = transactionEntity.getExternalId();
        TransactionTypeEntity tt = TransactionTypeService.getTransactionTypeById(transactionEntity.getTransactionTypeId());
        this.transactionType = CalculationType.of(tt.getCalculation());
        this.amount = transactionEntity.getAmount();
        this.prevBalance = transactionEntity.getPrevBalance();
        this.newBalance = transactionEntity.getNewBalance();
        this.transactionTime = transactionEntity.getTransactionTime();
        this.booked = transactionEntity.isBooked();
    }

    public TransactionEntity toEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        //TODO should be an accountnumber String
        transactionEntity.setId(getId());
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId((Long) getAccount());
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setExternalId(getExternalId());
        TransactionTypeEntity tt = TransactionTypeService.getTransactionTypeByCalculationType(getTransactionTime().toString());
        transactionEntity.setTransactionTypeId(tt.getId());
        transactionEntity.setAmount(getAmount());
        transactionEntity.setPrevBalance(getPrevBalance());
        transactionEntity.setNewBalance(getNewBalance());
        transactionEntity.setTransactionTime(getTransactionTime());
        transactionEntity.setBooked(isBooked());
        return transactionEntity;
    }
}

