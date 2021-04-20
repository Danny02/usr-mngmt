package dev.nullzwo.user.domain.model;

import io.vavr.collection.Seq;

import static dev.nullzwo.user.domain.model.Permission.*;
import static io.vavr.API.Seq;

public enum Role {
    CONTRACT_READ(READ),
    CONTRACT_WRITE(READ, WRITE),
    SIGNATORY(READ, SIGN),
    ADMIN();

    private final Seq<Permission> permissions;

    Role(Permission... permissions) {
        this.permissions = Seq(permissions);
    }
}
