package dev.nullzwo.user.domain.api;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import dev.nullzwo.user.domain.spi.ChangePort;
import dev.nullzwo.user.domain.spi.UserPort;
import io.vavr.collection.Seq;

public class ChangeService {

    private final ChangePort changePort;
    private final UserPort userPort;

    public ChangeService(ChangePort changePort, UserPort userPort) {
        this.changePort = changePort;
        this.userPort = userPort;
    }

    /**
     * Get an overview of all users which existed at some point in time.
     *
     * @return basic user info and count of change events
     */
    public Seq<UserChanges> getUserChangesOverview() {
        return changePort.getChangeCountByUser();
    }

    /**
     * Get all changes concerning a specific user
     *
     * @param userId id of the user
     * @return list of changes
     */
    public Seq<Change> getChangesOfUser(User.Id userId) {
        return changePort.changesOfUser(userId);
    }
}
