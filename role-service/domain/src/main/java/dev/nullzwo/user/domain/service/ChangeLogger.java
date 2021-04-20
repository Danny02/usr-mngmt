package dev.nullzwo.user.domain.service;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.spi.ChangePort;

import java.time.Clock;

public class ChangeLogger {
    private final Clock clock;
    private final ChangePort changePort;

    public ChangeLogger(Clock clock, ChangePort changePort) {
        this.clock = clock;
        this.changePort = changePort;
    }

    /**
     * Log a change to a user.
     *
     * @param user which was changed
     * @param event type of change
     */
    public void log(User user, Change.Event event) {
        var change = new Change(user.getId(), user.getPseudonym(), clock.instant(), event);
        changePort.save(change);
    }
}
