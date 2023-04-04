package org.delfin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class TransactionNotFoundException extends EntityNotFoundException {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionNotFoundException.class);

    public TransactionNotFoundException(Long transactionId) {
        LOG.error("Could not find transaction with ID " + transactionId);
    }
}
