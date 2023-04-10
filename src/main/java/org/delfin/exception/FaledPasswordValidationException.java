package org.delfin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class FaledPasswordValidationException extends Throwable {
    private static final Logger LOG = LoggerFactory.getLogger(FaledPasswordValidationException.class);
    public FaledPasswordValidationException(String username) {
        LOG.error("Failed login attempt for user with ID " + username);
    }
}
