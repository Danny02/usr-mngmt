package dev.nullzwo.user.domain.spi;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import io.vavr.collection.Seq;

public interface ChangePort {

    /**
     * Persist the following change to an user.
     *
     * @param change to persist
     */
    void save(Change change);

    /**
     * Get an count of events for all users for which an change was persisted anytime in the past.
     *
     * @return list of change counts per user
     */
    Seq<UserChanges> getChangeCountByUser();

    /**
     * Get all changes of an user. The changes are sorted by the time they occured.
     *
     * @param userId id of user
     * @return list of changes
     */
    Seq<Change> changesOfUser(User.Id userId);
}
