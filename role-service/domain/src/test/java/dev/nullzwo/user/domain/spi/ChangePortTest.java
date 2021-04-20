package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.nullzwo.user.domain.model.Change.Event.*;
import static io.vavr.API.Seq;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;

public interface ChangePortTest {
    User.Id FOO_ID = new User.Id("foo");
    User.Id BAR_ID = new User.Id("bar");

    ChangePort port();

    @Test
    @DisplayName("an change can be saved multiple times")
    default void saveChangeTwice() {
        var change = new Change(FOO_ID, "asd", now(), DELETED);

        port().save(change);
        port().save(change);

        assertThat(port().changesOfUser(FOO_ID)).hasSize(2);
    }

    @Test
    @DisplayName("count by user is empty by default")
    default void emptyCounts() {
        assertThat(port().getChangeCountByUser()).isEmpty();
    }

    @Test
    @DisplayName("counts per user")
    default void countByUser() {
        var changes = Seq(
                new Change(FOO_ID, "asd", now(), CREATED),
                new Change(FOO_ID, "asd", now().plusSeconds(1), EDITED),
                new Change(FOO_ID, "asd", now().plusSeconds(2), DELETED),
                new Change(BAR_ID, "dfg", now(), CREATED)
        );

        changes.forEach(port()::save);

        assertThat(port().getChangeCountByUser()).containsOnly(
                new UserChanges(FOO_ID, "asd", 3),
                new UserChanges(BAR_ID, "dfg", 1)
        );
    }

    @Test
    @DisplayName("changes of user are empty by default")
    default void emptyChanges() {
        assertThat(port().changesOfUser(FOO_ID)).isEmpty();
    }

    @Test
    @DisplayName("changes are ordered by event time")
    default void orderedChanges() {
        var changes = Seq(
                new Change(FOO_ID, "asd", now(), CREATED),
                new Change(FOO_ID, "asd", now().plusSeconds(1), EDITED),
                new Change(FOO_ID, "asd", now().plusSeconds(2), DELETED)
        );

        changes.forEach(port()::save);

        assertThat(port().changesOfUser(FOO_ID)).extracting(c -> c.getEvent()).containsExactly(CREATED, EDITED, DELETED);
    }

    @Test
    @DisplayName("returns only changes of user and as saved")
    default void onlyChangesByUser() {
        var changes = Seq(
                new Change(FOO_ID, "asd", now(), CREATED),
                new Change(FOO_ID, "asd", now().plusSeconds(1), EDITED),
                new Change(FOO_ID, "asd", now().plusSeconds(2), DELETED)
        );

        changes.forEach(port()::save);
        port().save(new Change(BAR_ID, "dfg", now(), CREATED));

        assertThat(port().changesOfUser(FOO_ID)).containsExactlyElementsOf(changes);
    }
}
