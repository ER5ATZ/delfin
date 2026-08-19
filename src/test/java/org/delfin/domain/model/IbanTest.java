package org.delfin.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IbanTest {

    @Test
    void generate_withValidInputs_producesValidIban() {
        String countryCode = "DE";
        String bankCode = "3704";
        String accountNumber = "0044053201";

        Iban iban = Iban.generate(countryCode, bankCode, accountNumber);

        assertThat(iban.getValue()).startsWith("DE");
        assertThat(iban.getValue()).hasSize(18); // DE code (2) + check digits (2) + bankCode (4) + accountNumber (10) = 18
    }

    @Test
    void isValid_withKnownValidIban_returnsTrue() {
        String validIban = "DE89370400440532013000";

        assertThat(Iban.isValid(validIban)).isTrue();
    }

    @Test
    void isValid_withAnotherKnownValidIban_returnsTrue() {
        String validIban = "GB29NWBK60161331926819";

        assertThat(Iban.isValid(validIban)).isTrue();
    }

    @Test
    void isValid_withInvalidChecksum_returnsFalse() {
        String invalidIban = "DE89370400440532013001"; // Last digit changed

        assertThat(Iban.isValid(invalidIban)).isFalse();
    }

    @Test
    void isValid_withNullIban_returnsFalse() {
        assertThat(Iban.isValid(null)).isFalse();
    }

    @Test
    void isValid_withEmptyIban_returnsFalse() {
        assertThat(Iban.isValid("")).isFalse();
    }

    @Test
    void isValid_withTooShortIban_returnsFalse() {
        assertThat(Iban.isValid("DE89")).isFalse();
    }

    @Test
    void isValid_withTooLongIban_returnsFalse() {
        String tooLong = "DE89" + "0".repeat(31); // 35 characters, max is 34

        assertThat(Iban.isValid(tooLong)).isFalse();
    }

    @Test
    void generate_producesValidIban_confirmedByIsValid() {
        Iban generated = Iban.generate("DE", "3704", "0044053201");

        assertThat(Iban.isValid(generated.getValue())).isTrue();
    }

    @Test
    void equals_sameValue_returnsTrue() {
        Iban iban1 = new Iban("DE89370400440532013000");
        Iban iban2 = new Iban("DE89370400440532013000");

        assertThat(iban1).isEqualTo(iban2);
    }

    @Test
    void equals_differentValue_returnsFalse() {
        Iban iban1 = new Iban("DE89370400440532013000");
        Iban iban2 = new Iban("DE89370400440532013001");

        assertThat(iban1).isNotEqualTo(iban2);
    }
}
