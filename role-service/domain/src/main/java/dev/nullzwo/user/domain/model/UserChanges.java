package dev.nullzwo.user.domain.model;

import java.util.Objects;

public class UserChanges {
    private final User.Id userId;
    private final String pseudonym;
    private final int changeCount;

    public UserChanges(User.Id userId, String pseudonym, int changeCount) {
        this.userId = userId;
        this.pseudonym = pseudonym;
        this.changeCount = changeCount;
    }

    public User.Id getUserId() {
        return userId;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChanges that = (UserChanges) o;
        return changeCount == that.changeCount && userId.equals(that.userId) && pseudonym.equals(that.pseudonym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pseudonym, changeCount);
    }

    @Override
    public String toString() {
        return "UserChanges{" +
                "userId=" + userId +
                ", pseudonym='" + pseudonym + '\'' +
                ", changeCount=" + changeCount +
                '}';
    }
}
