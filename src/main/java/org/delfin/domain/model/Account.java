package org.delfin.domain.model;

import jakarta.persistence.*;
import org.delfin.domain.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Embedded
    private Iban iban;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "overdraft_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "overdraft_limit_currency"))
    })
    private Money overdraftLimit;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Version
    private long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Account() {
    }

    public Account(UUID customerId, Iban iban, Currency currency, Money overdraftLimit) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.iban = iban;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void validateDebit(Money amount, Money currentBalance) {
        Money resultingBalance = currentBalance.subtract(amount);
        Money negatedLimit = overdraftLimit.negate();
        if (resultingBalance.getAmount().compareTo(negatedLimit.getAmount()) < 0) {
            throw new InsufficientFundsException(amount, currentBalance, overdraftLimit);
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public Iban getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money getOverdraftLimit() {
        return overdraftLimit;
    }

    public boolean isActive() {
        return active;
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
