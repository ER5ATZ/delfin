package org.delfin.model;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public enum Currency {
    EUR("Euro", "€", "EUR"),
    USD("US Dollar", "$", "USD");

    private final String code;
    private final String symbol;
    private final String description;

    private Currency(String description, String symbol, String code) {
        this.description = description;
        this.symbol = symbol;
        this.code = code;
    }

    public String code() {
        return  this.code;
    }

    public String symbol() {
        return this.symbol;
    }

    public String description() {
        return this.description;
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
