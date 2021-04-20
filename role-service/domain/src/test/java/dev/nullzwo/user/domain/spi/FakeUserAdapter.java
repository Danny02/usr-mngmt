package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static io.vavr.API.*;

public class FakeUserAdapter implements UserPort {
    private Map<User.Id, User> users = Map();

    @Override
    public void deleteById(User.Id userId) {
        users = users.remove(userId);
    }

    @Override
    public void setRoles(User.Id userId, Set<Role> roles) {
        users = users.mapValues(u -> u.getId().equals(userId) ? u.withRoles(roles) : u);
    }

    @Override
    public Option<User> findById(User.Id userId) {
        return users.get(userId);
    }

    @Override
    public Option<User.Id> create(User user) {
        if (user.getId() != null || user.getPseudonym() == null || user.getPseudonym().isBlank()) {
            throw new IllegalArgumentException();
        } else if (users.exists(t -> t._2.getPseudonym().equals(user.getPseudonym()))) {
            return None();
        } else {
            var id = new User.Id(UUID.randomUUID().toString());
            users = users.put(id, user.withId(id));
            return Some(id);
        }
    }

    @Override
    public Seq<User> findAll() {
        return users.values();
    }

    public static class FakeTest implements UserPortTest {

        UserPort port;

        @BeforeEach
        void setupContext() {
            port = new FakeUserAdapter();
        }

        @Override
        public UserPort port() {
            return port;
        }
    }
}
