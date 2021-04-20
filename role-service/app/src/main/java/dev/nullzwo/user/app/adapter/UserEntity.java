package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.model.Role;
import dev.nullzwo.user.domain.model.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.ALL;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "pseudonym")
)
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String id;

    private String pseudonym;

    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    @Enumerated(STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @JoinColumn(name = "user_id")
    @OnDelete(action = CASCADE)
    @Cascade(ALL)
    private Set<Role> roles = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(User u) {
        this.id = u.getId().getValue();
        this.pseudonym = u.getPseudonym();
        this.roles = u.getRoles().toJavaSet();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", roles=" + roles +
                '}';
    }
}
