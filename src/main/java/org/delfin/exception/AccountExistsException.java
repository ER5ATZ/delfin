package org.delfin.exception;

import org.delfin.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class AccountExistsException extends Throwable {
    private static final Logger LOG = LoggerFactory.getLogger(AccountExistsException.class);

    public AccountExistsException(Account prompt) {
        LOG.warn("Attempt to create new account with existing ID: " + prompt);
    }
}
