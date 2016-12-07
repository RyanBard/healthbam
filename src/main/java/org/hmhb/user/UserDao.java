package org.hmhb.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Database access object for {@link User} objects.
 *
 * spring-data-jpa is providing the implementation of this dao:
 * http://docs.spring.io/spring-data/jpa/docs/1.5.1.RELEASE/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 */
public interface UserDao extends CrudRepository<User, Long> {

    /**
     * Returns the {@link User}s associated with the passed in email.
     *
     * @return the {@link User}s
     */
    User findByEmailIgnoreCase(String email);

    /**
     * Returns all {@link User}s ordered ascending by their display names,
     * then emails.
     *
     * @return all {@link User}s
     */
    List<User> findAllByOrderByDisplayNameAscEmailAsc();

}
