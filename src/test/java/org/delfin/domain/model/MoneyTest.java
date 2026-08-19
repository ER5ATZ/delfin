package org.delfin.domain.model;

import org.delfin.domain.exception.CurrencyMismatchException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Test
    void add_sameCurrency_returnsNewMoneyWithSummedAmount() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("50.00"), Currency.EUR);

        Money result = money1.add(money2);

        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(result.getCurrency()).isEqualTo(Currency.EUR);
    }

    @Test
    void add_differentCurrency_throwsCurrencyMismatchException() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("50.00"), Currency.USD);

        assertThatThrownBy(() -> money1.add(money2))
                .isInstanceOf(CurrencyMismatchException.class)
                .hasMessageContaining("EUR")
                .hasMessageContaining("USD");
    }

    @Test
    void subtract_sameCurrency_returnsNewMoneyWithDifference() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("30.00"), Currency.EUR);

        Money result = money1.subtract(money2);

        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("70.00"));
        assertThat(result.getCurrency()).isEqualTo(Currency.EUR);
    }

    @Test
    void subtract_differentCurrency_throwsCurrencyMismatchException() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("50.00"), Currency.GBP);

        assertThatThrownBy(() -> money1.subtract(money2))
                .isInstanceOf(CurrencyMismatchException.class);
    }

    @Test
    void negate_positiveMoney_returnsNegativeAmount() {
        Money money = new Money(new BigDecimal("100.00"), Currency.EUR);

        Money result = money.negate();

        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("-100.00"));
        assertThat(result.getCurrency()).isEqualTo(Currency.EUR);
    }

    @Test
    void negate_negativeMoney_returnsPositiveAmount() {
        Money money = new Money(new BigDecimal("-50.00"), Currency.USD);

        Money result = money.negate();

        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(result.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    void isNegative_negativeMoney_returnsTrue() {
        Money money = new Money(new BigDecimal("-1.00"), Currency.EUR);

        assertThat(money.isNegative()).isTrue();
    }

    @Test
    void isNegative_positiveMoney_returnsFalse() {
        Money money = new Money(new BigDecimal("100.00"), Currency.EUR);

        assertThat(money.isNegative()).isFalse();
    }

    @Test
    void isNegative_zeroMoney_returnsFalse() {
        Money money = new Money(BigDecimal.ZERO, Currency.EUR);

        assertThat(money.isNegative()).isFalse();
    }

    @Test
    void isZero_zeroMoney_returnsTrue() {
        Money money = new Money(BigDecimal.ZERO, Currency.EUR);

        assertThat(money.isZero()).isTrue();
    }

    @Test
    void isZero_nonZeroMoney_returnsFalse() {
        Money money = new Money(new BigDecimal("0.01"), Currency.EUR);

        assertThat(money.isZero()).isFalse();
    }

    @Test
    void equals_sameAmountAndCurrency_returnsTrue() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("100.00"), Currency.EUR);

        assertThat(money1).isEqualTo(money2);
    }

    @Test
    void equals_differentAmount_returnsFalse() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("99.99"), Currency.EUR);

        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    void equals_differentCurrency_returnsFalse() {
        Money money1 = new Money(new BigDecimal("100.00"), Currency.EUR);
        Money money2 = new Money(new BigDecimal("100.00"), Currency.USD);

        assertThat(money1).isNotEqualTo(money2);
    }
}
