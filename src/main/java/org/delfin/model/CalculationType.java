package org.delfin.model;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public enum CalculationType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL;

    public static CalculationType of(String transactionType) {
        for (CalculationType tt : CalculationType.values()) {
            if (tt.toString().equals(transactionType.toUpperCase())) return tt;
        }

        return NEUTRAL;
    }
}
