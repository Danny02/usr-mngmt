package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public interface UserPort {

    /**
     * Delete an User.
     *
     * @param userId id of user
     */
    void deleteById(User.Id userId);

    /**
     * Overwrite roles of an user.
     *
     * @param userId id of user
     * @param roles  to overwrite current once with
     */
    void setRoles(User.Id userId, Set<Role> roles);

    /**
     * Lookup user by Id.
     *
     * @param userId id of user
     * @return Some user with a user with given id exists.
     */
    Option<User> findById(User.Id userId);

    /**
     * Creates a new user. The id field must not be set.
     *
     * @param user data to create the user with
     * @return an user id if it was possible to create the user (all constraints where met).
     */
    Option<User.Id> create(User user);

    /**
     * Get all current users.
     *
     * @return a list of users
     */
    Seq<User> findAll();
}
