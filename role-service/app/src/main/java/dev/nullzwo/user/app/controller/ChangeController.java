package dev.nullzwo.user.app.controller;

import de.otto.edison.hal.HalRepresentation;
import dev.nullzwo.user.domain.api.ChangeService;
import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static dev.nullzwo.user.app.controller.UserController.USER_PATH;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ChangeController {

    public static final String CHANGES_PATH = "/changes";
    public static final String CHANGES_POSTFIX = "/changes";

    private final ChangeService service;

    public ChangeController(ChangeService service) {
        this.service = service;
    }

    @GetMapping(USER_PATH + "/{id}" + CHANGES_POSTFIX)
    public ResponseEntity<Changes> getChanges(@PathVariable String id) {
        var changes = service.getChangesOfUser(new User.Id(id));
        if (changes.isEmpty()) {
            return notFound().build();
        } else {
            return ok(new Changes(changes.asJava()));
        }
    }

    @GetMapping(CHANGES_PATH)
    public HalRepresentation getChangesOverview() {
        return UserRepresentation.changes(service.getUserChangesOverview());
    }

    public static class Changes {
        private final List<Change> changes;

        public Changes(List<Change> changes) {
            this.changes = changes;
        }

        public List<Change> getChanges() {
            return changes;
        }
    }
}
