package dev.nullzwo.user.domain.api;

import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.service.ChangeLogger;
import dev.nullzwo.user.domain.spi.UserPort;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Validation;

import static dev.nullzwo.user.domain.api.UserService.Reason.*;
import static dev.nullzwo.user.domain.model.Change.Event.*;
import static io.vavr.API.Some;
import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

public class UserService {

    private final UserPort userPort;
    private final ChangeLogger logger;

    public UserService(UserPort userPort, ChangeLogger logger) {
        this.userPort = userPort;
        this.logger = logger;
    }

    /**
     * Get all current users.
     *
     * @return list of users
     */
    public Seq<User> getUsers() {
        return userPort.findAll();
    }

    /**
     * Create a new user. A user needs to have a unique pseudonym and at least one role.
     *
     * @param pseudonym of the user
     * @param roles     of the user
     * @return either valid when the user was created or invalid with the reason why
     */
    public Validation<Reason, User> create(String pseudonym, Set<Role> roles) {
        if (pseudonym.isBlank()) {
            return invalid(EMPTY_PSEUDONYM);
        }

        return validateRoles(roles).flatMap(rs -> {
            var user = new User(null, pseudonym, roles);
            var created = userPort.create(user).map(user::withId);
            created.forEach(u -> logger.log(u, CREATED));
            return created.toValidation(DUPLICATE_PSEUDONYM);
        });
    }

    /**
     * Overwrites the roles of a specific user.
     *
     * @param userId id of user to change the roles
     * @param roles  replacement of the old roles
     * @return either valid when the user could be changed or invalid with the reason why
     */
    public Validation<Reason, User> changeRoles(User.Id userId, Set<Role> roles) {
        return validateRoles(roles).flatMap(rs -> {
            userPort.setRoles(userId, rs);
            var vali = userPort.findById(userId).toValidation(NON_EXISTING);
            vali.forEach(u -> logger.log(u, EDITED));
            return vali;
        });
    }

    /**
     * Delete a specific user.
     *
     * @param userId id of the user to delete
     * @return None if the user was deleted or some reason why not
     */
    public Option<Reason> delete(User.Id userId) {
        return userPort.findById(userId).map(u -> {
            userPort.deleteById(userId);
            logger.log(u, DELETED);
            return Option.<Reason>none();
        }).getOrElse(Some(NON_EXISTING));
    }

    private Validation<Reason, Set<Role>> validateRoles(Set<Role> roles) {
        return roles.isEmpty() ? invalid(MIN_ROLE_COUNT) : valid(roles);
    }

    public enum Reason {
        NON_EXISTING, DUPLICATE_PSEUDONYM, EMPTY_PSEUDONYM, MIN_ROLE_COUNT
    }
}
