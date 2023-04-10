package org.delfin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class UserNotFoundException extends EntityNotFoundException {
    private static final Logger LOG = LoggerFactory.getLogger(UserNotFoundException.class);
    public UserNotFoundException(String username) {
        LOG.error("Could not find user with username " + username);
    }

    public UserNotFoundException(Long id) {
        LOG.error("Could not find user with ID " + id);
    }
}
