package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.nullzwo.user.domain.model.Role.ADMIN;
import static dev.nullzwo.user.domain.model.Role.CONTRACT_READ;
import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public interface UserPortTest {
    User.Id UNKOWN_ID = new User.Id("unkown");
    User USER = new User(null, "pseudo", Set(ADMIN));

    UserPort port();

    @Test
    @DisplayName("empty result when id unkown")
    default void emptyWhenUnkown() {
        assertThat(port().findById(UNKOWN_ID)).isEmpty();
    }

    @Test
    @DisplayName("creation generates id")
    default void generateId() {
        var id = port().create(USER);
        assertThat(id).isDefined();
    }

    @Test
    @DisplayName("creation generates uniq id")
    default void uniqId() {
        var id1 = port().create(USER);
        var id2 = port().create(USER.withPseudonym("other"));
        assertThat(id1.get()).isNotEqualTo(id2.get());
    }

    @Test
    @DisplayName("find correct user")
    default void findCorrect() {
        port().create(new User(null, "other", Set()));
        var id = port().create(USER).get();
        assertThat(port().findById(id)).contains(USER.withId(id));
    }

    @Test
    @DisplayName("id can not be set before creation")
    default void unsetIdOnCreate() {
        assertThatThrownBy(() -> port().create(new User(UNKOWN_ID, "foo", Set()))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("pseudonym is required")
    default void throwIfPseudonymUnset() {
        assertThatThrownBy(() -> port().create(new User(null, null, Set()))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("pseudonym must not be empty")
    default void throwIfPseudonymEmpty() {
        assertThatThrownBy(() -> port().create(new User(null, "", Set()))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("can not create if pseudo exists")
    default void pseudoExists() {
        port().create(USER);
        var id = port().create(USER);
        assertThat(id).isEmpty();
    }

    @Test
    @DisplayName("can delete nonexisting")
    default void falseWhenUnkown() {
        port().deleteById(UNKOWN_ID);
    }

    @Test
    @DisplayName("can not find deleted")
    default void canNotFindDeleted() {
        var id = port().create(USER).get();
        port().deleteById(id);
        assertThat(port().findById(id)).isEmpty();
    }

    @Test
    @DisplayName("delete does not change other")
    default void deleteNoOther() {
        var id = port().create(USER).get();
        port().deleteById(UNKOWN_ID);
        assertThat(port().findById(id)).isDefined();
    }

    @Test
    @DisplayName("find all created")
    default void findAllCreated() {
        port().create(USER);
        port().create(USER.withPseudonym("other"));

        assertThat(port().findAll()).extracting(u -> u.getPseudonym()).containsOnly("pseudo", "other");
    }

    @Test
    @DisplayName("set role of unkown does not throw")
    default void setRoleOfUnkown() {
        port().setRoles(UNKOWN_ID, Set(ADMIN));
    }

    @Test
    @DisplayName("set role overrides roles")
    default void setRoleOfKown() {
        var id = port().create(USER.withRoles(Set(CONTRACT_READ))).get();
        port().setRoles(id, Set(ADMIN));
        assertThat(port().findById(id)).hasValueSatisfying(user -> {
            assertThat(user.getRoles()).containsOnly(ADMIN);
        });
    }
}