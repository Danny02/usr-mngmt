package dev.nullzwo.user.app.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.otto.edison.hal.Embedded;
import de.otto.edison.hal.HalRepresentation;
import de.otto.edison.hal.Link;
import de.otto.edison.hal.Links;
import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;
import io.vavr.collection.Seq;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static dev.nullzwo.user.app.controller.ChangeController.CHANGES_PATH;
import static dev.nullzwo.user.app.controller.ChangeController.CHANGES_POSTFIX;
import static dev.nullzwo.user.app.controller.UserController.ROLES_POSTFIX;
import static dev.nullzwo.user.app.controller.UserController.USER_PATH;

public class UserRepresentation extends HalRepresentation {
    public final String pseudonym;

    @JsonInclude(NON_NULL)
    public final Set<String> roles;

    @JsonInclude(NON_NULL)
    public final Integer eventCount;

    public UserRepresentation(User user) {
        super(Links.linkingTo()
                .self(self(user))
                .single(Link.link("roles", self(user) + ROLES_POSTFIX))
                .build());
        this.pseudonym = user.getPseudonym();
        this.roles = user.getRoles().map(Enum::name).toJavaSet();
        eventCount = null;
    }

    public UserRepresentation(UserChanges changes) {
        super(Links.linkingTo()
                .single(Link.link("changes", self(changes.getUserId()) + CHANGES_POSTFIX))
                .build());
        this.pseudonym = changes.getPseudonym();
        roles = null;
        eventCount = changes.getChangeCount();
    }

    private static String self(User user) {
        return self(user.getId());
    }

    private static String self(User.Id id) {
        return USER_PATH + "/" + id.getValue();
    }

    public static HalRepresentation collection(Seq<User> users) {
        var builder = Links.linkingTo().self(USER_PATH);
        for (var user : users) {
            builder.item(self(user));
        }
        var embedded = Embedded.embeddedBuilder();
        if (!users.isEmpty()) {
            embedded.with("item", users.map(UserRepresentation::new).asJava());
        }
        return new HalRepresentation(builder.build(), embedded.build());
    }

    public static HalRepresentation changes(Seq<UserChanges> users) {
        var builder = Links.linkingTo().self(CHANGES_PATH);
        for (var user : users) {
            builder.item(self(user.getUserId()));
        }
        var embedded = Embedded.embeddedBuilder();
        if (!users.isEmpty()) {
            embedded.with("item", users.map(UserRepresentation::new).asJava());
        }
        return new HalRepresentation(builder.build(), embedded.build());
    }
}
