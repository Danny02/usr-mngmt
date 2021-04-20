package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import dev.nullzwo.user.domain.spi.ChangePort;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import org.springframework.stereotype.Component;

@Component
public class SpringChangeAdapter implements ChangePort {

    private final JpaChangeRepository repository;

    public SpringChangeAdapter(JpaChangeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Change change) {
        repository.save(new ChangeEntity(change));
    }

    @Override
    public Seq<UserChanges> getChangeCountByUser() {
        return Vector.ofAll(repository.changeCountByUser())
                .map(UserChangesDto::toUserChanges);
    }

    @Override
    public Seq<Change> changesOfUser(User.Id userId) {
        return Vector.ofAll(repository.findByUserid(userId.getValue()))
                .map(ChangeEntity::toChange);
    }
}
