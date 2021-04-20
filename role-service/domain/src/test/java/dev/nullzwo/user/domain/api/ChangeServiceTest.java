package dev.nullzwo.user.domain.api;

import dev.nullzwo.user.domain.api.TestContext.MockClock;
import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.UserChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.nullzwo.user.domain.model.Change.Event.*;
import static dev.nullzwo.user.domain.model.Role.*;
import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangeServiceTest {

    MockClock clock;
    UserService userService;
    ChangeService changeService;

    @BeforeEach
    void setupContext() {
        var ctx = new TestContext();
        clock = ctx.clock;
        userService = ctx.userService;
        changeService = ctx.changeService;
    }

    @Test
    @DisplayName("should create events in user lifecycle")
    void eventOnCreate() {
        var id = userService.create("foo", Set(ADMIN)).get().getId();
        userService.changeRoles(id, Set(CONTRACT_READ));
        userService.changeRoles(id, Set(SIGNATORY));
        userService.delete(id);

        assertThat(changeService.getChangesOfUser(id))
                .extracting(Change::getEvent)
                .containsExactly(CREATED, EDITED, EDITED, DELETED);
    }

    @Test
    @DisplayName("reuse of pseudonym results in new change log")
    void reuseOfPseudonym() {
        var id = userService.create("foo", Set(ADMIN)).get().getId();
        userService.delete(id);
        var newid = userService.create("foo", Set(ADMIN)).get().getId();

        var a = assertThat(changeService.getUserChangesOverview()).hasSize(2);
        a.extracting(UserChanges::getPseudonym).containsOnly("foo");
        a.extracting(UserChanges::getChangeCount).containsOnly(2, 1);

        assertThat(changeService.getChangesOfUser(newid)).hasSize(1);
    }

    @Test
    @DisplayName("uses current timestamp")
    void currentTimestamp() {
        var id = userService.create("foo", Set(ADMIN)).get().getId();
        assertThat(changeService.getChangesOfUser(id)).first().extracting(Change::getInstant).isEqualTo(clock.instant());
    }
}
