package org.delfin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class CustomerNotFoundException extends EntityNotFoundException {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerNotFoundException.class);
    public CustomerNotFoundException(Long customerId) {
        LOG.error("Could not find customer with ID " + customerId);
    }
}
