package org.delfin.model;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public enum Currency {
    EUR("Euro", "", "EUR"),
    USD("US Dollar", "$", "USD");

    private String code;
    private String symbol;
    private String description;

    private Currency(String description, String symbol, String code) {
    }

    public Currency findByCode(String code) {
        for (Currency c: Currency.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        return EUR;
    }
}
