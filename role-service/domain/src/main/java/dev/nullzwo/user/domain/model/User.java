package dev.nullzwo.user.domain.model;

import io.vavr.collection.Set;

import java.util.Objects;

public class User {
    private final Id id;
    private final String pseudonym;
    private final Set<Role> roles;

    public User(Id id, String pseudonym, Set<Role> roles) {
        this.id = id;
        this.pseudonym = pseudonym;
        this.roles = roles;
    }

    public Id getId() {
        return id;
    }

    public User withId(Id id) {
        return new User(id, pseudonym, roles);
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public User withPseudonym(String pseudonym) {
        return new User(id, pseudonym, roles);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User withRoles(Set<Role> roles) {
        return new User(id, pseudonym, roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && pseudonym.equals(user.pseudonym) && roles.equals(user.roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", pseudonym='" + pseudonym + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pseudonym, roles);
    }

    public static class Id {
        private final String value;

        public Id(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return value.equals(id.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Id{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }
}
