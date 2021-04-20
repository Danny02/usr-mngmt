package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.model.Change;
import dev.nullzwo.user.domain.model.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "changes")
public class ChangeEntity {
    @Id
    @GeneratedValue
    private long id;
    private String userid;
    private String pseudonym;
    private Instant instant;

    @Enumerated(STRING)
    private Change.Event event;

    public ChangeEntity() {
    }

    public ChangeEntity(Change c) {
        this.userid = c.getId().getValue();
        this.pseudonym = c.getPseudonym();
        this.instant = c.getInstant();
        this.event = c.getEvent();
    }

    public Change toChange() {
        return new Change(new User.Id(userid), pseudonym, instant, event);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Change.Event getEvent() {
        return event;
    }

    public void setEvent(Change.Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeEntity that = (ChangeEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ChangeEntity{" +
                "id='" + id + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", instant=" + instant +
                ", event=" + event +
                '}';
    }
}
