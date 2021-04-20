package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.BeforeEach;

import static io.vavr.API.Seq;

public class FakeChangeAdapter implements ChangePort {

    private Seq<Change> changes = Seq();

    @Override
    public void save(Change change) {
        changes = changes.append(change);
    }

    @Override
    public Seq<UserChanges> getChangeCountByUser() {
        return changes.groupBy(c -> c.getId()).map(t -> new UserChanges(t._1, t._2.head().getPseudonym(), t._2.size()));
    }

    @Override
    public Seq<Change> changesOfUser(User.Id userId) {
        return changes.filter(c -> c.getId().equals(userId));
    }

    public static class FakeTest implements ChangePortTest {
        FakeChangeAdapter adapter;

        @BeforeEach
        void setupContext() {
            adapter = new FakeChangeAdapter();
        }

        @Override
        public ChangePort port() {
            return adapter;
        }
    }
}
