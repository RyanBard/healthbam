package org.hmhb.authentication;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import org.hmhb.user.User;

/**
 * Service to generate and validate our JWT tokens.
 */
public interface JwtAuthenticationService {

    /**
     * Generate a JWT token from the passed in {@link User} info.
     *
     * @param user the {@link User} info to put into a JWT token
     * @return the JWT token
     */
    String generateJwtToken(
            @Nonnull User user
    );

    /**
     * Validate the JWT token in the request's Authorization header and put the
     * {@link User}'s info in the {@link HttpServletRequest}.
     *
     * Noop if no auth header is present.
     *
     * @param request the {@link HttpServletRequest} to look for auth header
     *                and potentially put the {@link User} into
     */
    void validateAuthentication(
            @Nonnull HttpServletRequest request
    );

    /**
     * Validate the JWT token and put the {@link User}'s info in the
     * {@link HttpServletRequest}.
     *
     * @param request the {@link HttpServletRequest} to potentially put
     *                the {@link User} into
     * @param authToken the JWT to validate
     */
    void validateToken(
            @Nonnull HttpServletRequest request,
            @Nonnull String authToken
    );

}
