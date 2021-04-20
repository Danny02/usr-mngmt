package dev.nullzwo.user.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Change {
    private final User.Id id;
    private final String pseudonym;
    private final Instant instant;
    private final Event event;

    public Change(User.Id id, String pseudonym, Instant instant, Event event) {
        this.id = id;
        this.pseudonym = pseudonym;
        this.instant = instant;
        this.event = event;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public Instant getInstant() {
        return instant;
    }

    public Event getEvent() {
        return event;
    }

    public User.Id getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change change = (Change) o;
        return id.equals(change.id) && pseudonym.equals(change.pseudonym) && instant.equals(change.instant) && event == change.event;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pseudonym, instant, event);
    }

    @Override
    public String toString() {
        return "Change{" +
                "id=" + id +
                ", pseudonym='" + pseudonym + '\'' +
                ", instant=" + instant +
                ", event=" + event +
                '}';
    }

    public enum Event {
        CREATED, EDITED, DELETED;
    }
}
