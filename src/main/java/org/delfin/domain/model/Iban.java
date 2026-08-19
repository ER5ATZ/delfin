package org.delfin.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigInteger;
import java.util.Objects;

@Embeddable
public class Iban {
    @Column(name = "iban")
    private String value;

    public Iban() {
    }

    public Iban(String value) {
        this.value = value;
    }

    public static Iban generate(String countryCode, String bankCode, String accountNumber) {
        String rearranged = bankCode + accountNumber + countryCode + "00";
        String numericIban = convertToNumeric(rearranged);
        BigInteger numericValue = new BigInteger(numericIban);
        BigInteger remainder = numericValue.mod(new BigInteger("97"));
        int checkDigit = 98 - remainder.intValue();
        String ibanString = countryCode + String.format("%02d", checkDigit) + bankCode + accountNumber;
        return new Iban(ibanString);
    }

    public static boolean isValid(String iban) {
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return false;
        }
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        String numericIban = convertToNumeric(rearranged);
        try {
            BigInteger numericValue = new BigInteger(numericIban);
            BigInteger remainder = numericValue.mod(new BigInteger("97"));
            return remainder.intValue() == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String convertToNumeric(String iban) {
        StringBuilder numeric = new StringBuilder();
        for (char c : iban.toCharArray()) {
            if (Character.isDigit(c)) {
                numeric.append(c);
            } else if (Character.isLetter(c)) {
                numeric.append(Character.getNumericValue(c));
            }
        }
        return numeric.toString();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iban iban = (Iban) o;
        return Objects.equals(value, iban.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
