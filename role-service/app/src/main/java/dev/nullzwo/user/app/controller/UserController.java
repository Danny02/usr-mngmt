package dev.nullzwo.user.app.controller;

import de.otto.edison.hal.HalRepresentation;
import dev.nullzwo.user.domain.api.UserService;
import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import io.vavr.collection.HashSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@RestController
@CrossOrigin
@RequestMapping(produces = {"application/hal+json", "application/json"})
public class UserController {
    public static final String USER_PATH = "/user";
    public static final String ROLES_POSTFIX = "/roles";

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(USER_PATH)
    public HalRepresentation users() {
        return UserRepresentation.collection(service.getUsers());
    }

    @PostMapping(USER_PATH)
    public ResponseEntity<Object> createUser(@RequestBody UserValues values) {
        var result = service.create(values.pseudonym, HashSet.ofAll(values.roles));
        return result.<ResponseEntity<Object>>map(user -> ok(new UserRepresentation(user)))
                .getOrElseGet(reason -> {
                    switch (reason) {
                        case NON_EXISTING:
                            return notFound().build();
                        case MIN_ROLE_COUNT:
                            return badRequest().body("at least on role is required");
                        case DUPLICATE_PSEUDONYM:
                            return badRequest().body("pseudonym does already exist");
                        case EMPTY_PSEUDONYM:
                            return badRequest().body("pseudonym can not be empty");
                        default:
                            return status(INTERNAL_SERVER_ERROR).build();
                    }
                });
    }

    @PutMapping(USER_PATH + "/{id}" + ROLES_POSTFIX)
    public ResponseEntity<Object> updateRoles(@PathVariable String id, @RequestBody Set<Role> roles) {
        var result = service.changeRoles(new User.Id(id), HashSet.ofAll(roles));
        return result.<ResponseEntity<Object>>map(user -> ok(new UserRepresentation(user)))
                .getOrElseGet(reason -> {
                    switch (reason) {
                        case NON_EXISTING:
                            return notFound().build();
                        case MIN_ROLE_COUNT:
                            return badRequest().body("at least on role is required");
                        default:
                            return status(INTERNAL_SERVER_ERROR).build();
                    }
                });
    }

    @DeleteMapping(USER_PATH + "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        var result = service.delete(new User.Id(id));
        return result.map(reason -> {
            switch (reason) {
                case NON_EXISTING:
                    return notFound();
                default:
                    return status(INTERNAL_SERVER_ERROR);
            }
        })
                .getOrElse(accepted())
                .build();
    }

    public static class UserValues {
        private final String pseudonym;
        private final Set<Role> roles;

        public UserValues(String pseudonym, Set<Role> roles) {
            this.pseudonym = pseudonym;
            this.roles = roles;
        }
    }
}
