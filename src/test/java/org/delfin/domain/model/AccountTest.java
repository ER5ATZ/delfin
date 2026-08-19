package org.delfin.domain.model;

import org.delfin.domain.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    @Test
    void validateDebit_withinBalance_succeeds() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(new BigDecimal("500.00"), Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("1000.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("500.00"), Currency.EUR);

        // Should not throw
        assertThatCode(() -> account.validateDebit(debitAmount, currentBalance))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDebit_resultingBalanceExactlyAtOverdraftLimit_succeeds() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(new BigDecimal("500.00"), Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("600.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("1100.00"), Currency.EUR);

        // Result would be -500 (exactly the limit)
        assertThatCode(() -> account.validateDebit(debitAmount, currentBalance))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDebit_exceedsOverdraftLimit_throwsInsufficientFundsException() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(new BigDecimal("500.00"), Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("700.00"), Currency.EUR);

        // Result would be -600 (exceeds -500 limit)
        assertThatThrownBy(() -> account.validateDebit(debitAmount, currentBalance))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");
    }

    @Test
    void validateDebit_withNegativeBalance_withinOverdraftLimit_succeeds() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(new BigDecimal("500.00"), Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("-200.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("100.00"), Currency.EUR);

        // Result would be -300 (within -500 limit)
        assertThatCode(() -> account.validateDebit(debitAmount, currentBalance))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDebit_zeroOverdraftLimit_allowsDebitOnlyToZero() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(BigDecimal.ZERO, Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("100.00"), Currency.EUR);

        // Result would be exactly 0
        assertThatCode(() -> account.validateDebit(debitAmount, currentBalance))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDebit_zeroOverdraftLimit_rejectDebitBelowZero() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(BigDecimal.ZERO, Currency.EUR);
        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        Money currentBalance = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money debitAmount = new Money(new BigDecimal("100.01"), Currency.EUR);

        assertThatThrownBy(() -> account.validateDebit(debitAmount, currentBalance))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void constructor_setsExpectedFields() {
        UUID customerId = UUID.randomUUID();
        Iban iban = new Iban("DE89370400440532013000");
        Money overdraftLimit = new Money(new BigDecimal("500.00"), Currency.EUR);

        Account account = new Account(customerId, iban, Currency.EUR, overdraftLimit);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getCustomerId()).isEqualTo(customerId);
        assertThat(account.getIban()).isEqualTo(iban);
        assertThat(account.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(account.getOverdraftLimit()).isEqualTo(overdraftLimit);
        assertThat(account.isActive()).isTrue();
    }
}
