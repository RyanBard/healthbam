package org.hmhb.user;

import javax.annotation.Nonnull;

import java.util.List;

import com.google.api.services.plus.model.Person;

/**
 * Service to create and retrieve {@link User}s.
 */
public interface UserService {

    /**
     * Updates an existing user, or creates a new non-admin {@link User} in
     * the system based on the data passed in from google.
     *
     * @param email the email to lookup or save into the {@link User}
     * @param profile the user's google+ profile
     * @return the {@link User}
     */
    User saveWithGoogleData(
            @Nonnull String email,
            @Nonnull Person profile
    );

    /**
     * Retrieves a {@link User} by its database ID.
     *
     * @param id the database ID
     * @return the {@link User}
     */
    User getById(
            @Nonnull Long id
    );

    /**
     * Retrieves all {@link User}s in the system.
     *
     * @return all {@link User}s
     */
    List<User> getAll();

    /**
     * Retrieves all {@link User}s in the system as a CSV.
     *
     * @param jwtToken the JWT auth token
     * @return all {@link User}s as CSV
     */
    String getAllAsCsv(
            @Nonnull String jwtToken
    );

    /**
     * Deletes a {@link User} by its database ID.
     *
     * @param id the database ID
     * @return the deleted {@link User}
     */
    User delete(
            @Nonnull Long id
    );

    /**
     * Saves a {@link User} (create or update).
     *
     * @param user the {@link User} to create or update
     * @return the saved {@link User}
     */
    User save(
            @Nonnull User user
    );

}
