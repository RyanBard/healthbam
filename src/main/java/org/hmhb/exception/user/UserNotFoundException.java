package org.hmhb.exception.user;

import org.hmhb.exception.NotFoundException;
import org.hmhb.user.User;

/**
 * Exception thrown when a {@link User} cannot be found.
 */
public class UserNotFoundException extends NotFoundException {

    /**
     * Constructs a {@link UserNotFoundException}.
     *
     * @param id the {@link User}'s database ID that wasn't found
     */
    public UserNotFoundException(Long id) {
        super("User wasn't found: id=" + id);
    }

}
