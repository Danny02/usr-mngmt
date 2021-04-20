package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.spi.UserPort;
import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.vavr.API.None;
import static io.vavr.API.Some;

@Component
public class SpringUserAdapter implements UserPort {

    private final JpaUserRepository repository;

    public SpringUserAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteById(User.Id userId) {
        repository.deleteById(userId.getValue());
    }

    @Override
    @Transactional
    public void setRoles(User.Id userId, Set<Role> roles) {
        repository.findById(userId.getValue()).ifPresent(entity -> {
            entity.setRoles(roles.toJavaSet());
            repository.save(entity);
        });
    }

    @Override
    public Option<User> findById(User.Id userId) {
        return Option.ofOptional(repository.findById(userId.getValue())).map(this::fromJpa);
    }

    @Override
    public Option<User.Id> create(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException();
        }

        var id = new User.Id(UUID.randomUUID().toString());
        var jpaUser = new UserEntity(user.withId(id));

        try {
            repository.save(jpaUser);
            return Some(id);
        } catch (DataIntegrityViolationException ex) {
            return None();
        }
    }

    @Override
    public Seq<User> findAll() {
        return Vector.ofAll(repository.findAll()).map(this::fromJpa);
    }

    private User fromJpa(UserEntity user) {
        return new User(new User.Id(user.getId()), user.getPseudonym(), HashSet.ofAll(user.getRoles()));
    }
}
