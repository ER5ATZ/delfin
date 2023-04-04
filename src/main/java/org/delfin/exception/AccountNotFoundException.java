package org.delfin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class AccountNotFoundException extends EntityNotFoundException {
    private static final Logger LOG = LoggerFactory.getLogger(AccountNotFoundException.class);
    public AccountNotFoundException(Long accountId) {
        LOG.error("Could not find account with ID " + accountId);
    }
}
