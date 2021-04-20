package dev.nullzwo.user.domain.api;

import dev.nullzwo.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.nullzwo.user.domain.api.UserService.Reason.*;
import static dev.nullzwo.user.domain.model.Role.*;
import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class UserServiceTest {

    UserService service;

    @BeforeEach
    void setupContext() {
        var ctx = new TestContext();
        service = ctx.userService;
    }

    @Test
    @DisplayName("should return no users without any other interactions")
    void startsWithoutUsers() {
        assertThat(service.getUsers()).isEmpty();
    }

    @Nested
    class CreateUser {

        @Test
        @DisplayName("when a user is created it gets returned by getUsers")
        void createSingleUser() {
            var vali = service.create("foo", Set(ADMIN));
            assertThat(vali).isValid();
            assertThat(service.getUsers()).containsOnly(vali.get());
        }

        @Test
        @DisplayName("when creating a user an id is generated")
        void generateUserId() {
            var user = service.create("foo", Set(ADMIN)).get();
            assertThat(user.getId()).extracting(User.Id::getValue).isNotNull();
        }

        @Test
        @DisplayName("when creating a user the generated id is uniq")
        void generateUniqUserId() {
            var user1 = service.create("foo", Set(ADMIN)).get();
            var user2 = service.create("bar", Set(ADMIN)).get();
            assertThat(user1.getId()).isNotEqualTo(user2.getId());
        }

        @Test
        @DisplayName("when a multiple users are created all get returned by getUsers")
        void createMultipleUser() {
            var created = Set("foo", "bar", "baz").flatMap(ps -> service.create(ps, Set(ADMIN)));
            assertThat(service.getUsers()).hasSameElementsAs(created);
        }

        @Test
        @DisplayName("a pseudonym must be uniq")
        void uniqPseudonym() {
            service.create("foo", Set(ADMIN));
            var vali = service.create("foo", Set(CONTRACT_READ));
            assertThat(vali).containsInvalid(DUPLICATE_PSEUDONYM);
        }

        @Test
        @DisplayName("a pseudonym must not be empty")
        void nonemptyPseudonym() {
            var vali = service.create("   ", Set(CONTRACT_READ));
            assertThat(vali).containsInvalid(EMPTY_PSEUDONYM);
        }

        @Test
        @DisplayName("a user must have at least one role")
        void minRoleCount() {
            var vali = service.create("foo", Set());
            assertThat(vali).containsInvalid(MIN_ROLE_COUNT);
        }
    }

    @Nested
    class ChangeRoles {

        User user;

        @BeforeEach
        void setupUser() {
            user = service.create("foo", Set(ADMIN)).get();
        }

        @Test
        @DisplayName("a role change overrides all previous values")
        void changeOverrides() {
            var newRoles = Set(CONTRACT_READ, SIGNATORY);
            var changed = service.changeRoles(user.getId(), newRoles).get();
            assertThat(changed.getRoles()).hasSameElementsAs(newRoles);
        }

        @Test
        @DisplayName("a user must have at least one role")
        void minRoleCount() {
            var vali = service.changeRoles(user.getId(), Set());
            assertThat(vali).containsInvalid(MIN_ROLE_COUNT);
        }

        @Test
        @DisplayName("pseudonym is unchaged by role change")
        void pseudonymUnchaged() {
            var changed = service.changeRoles(user.getId(), Set(CONTRACT_READ)).get();
            assertThat(changed.getPseudonym()).isEqualTo(user.getPseudonym());
        }

        @Test
        @DisplayName("id is unchaged by role change")
        void idUnchaged() {
            var changed = service.changeRoles(user.getId(), Set(CONTRACT_READ)).get();
            assertThat(changed.getId()).isEqualTo(user.getId());
        }

        @Test
        @DisplayName("change get returned by getUsers")
        void getChanged() {
            var changed = service.changeRoles(user.getId(), Set(CONTRACT_READ)).get();
            assertThat(service.getUsers()).containsOnly(changed);
        }

        @Test
        @DisplayName("non existing user can not be changed")
        void changeNonExisting() {
            var vali = service.changeRoles(new User.Id("nonexisting"), Set(CONTRACT_READ));
            assertThat(vali).containsInvalid(NON_EXISTING);
        }
    }

    @Nested
    class DeleteUser {

        User user;

        @BeforeEach
        void setupUser() {
            user = service.create("foo", Set(ADMIN)).get();
        }

        @Test
        @DisplayName("existing user can be deleted")
        void deleteResultOfExisting() {
            assertThat(service.delete(user.getId())).isEmpty();
        }

        @Test
        @DisplayName("can not delete non existing")
        void deleteResultOfNonExisting() {
            assertThat(service.delete(new User.Id("nonexisting"))).contains(NON_EXISTING);
        }

        @Test
        @DisplayName("can only delete once")
        void deleteResultAfterDeleted() {
            service.delete(user.getId());
            assertThat(service.delete(user.getId())).contains(NON_EXISTING);
        }

        @Test
        @DisplayName("deleted is not returned by getUsers")
        void deletedNotReturned() {
            service.delete(user.getId());
            assertThat(service.getUsers()).isEmpty();
        }

        @Test
        @DisplayName("deleting non existing does not changed other users")
        void deleteNonExistingDoesNotChangeOther() {
            service.delete(new User.Id("non existing"));
            assertThat(service.getUsers()).containsOnly(user);
        }

        @Test
        @DisplayName("deleting existing does not changed other users")
        void deleteExistingDoesNotChangeOther() {
            var other = service.create("bar", Set(ADMIN)).get();
            service.delete(user.getId());
            assertThat(service.getUsers()).containsOnly(other);
        }
    }
}
