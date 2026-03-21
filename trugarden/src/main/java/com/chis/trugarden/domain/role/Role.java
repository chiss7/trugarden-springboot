package com.chis.trugarden.domain.role;

import com.chis.trugarden.shared.enums.Roles;

import java.util.Objects;

public class Role {
    private final Long id;
    private final Roles name;

    public Role(Long id, Roles name) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Role name cannot be null");
    }

    public static Role of(Long id, Roles name) {
        return new Role(id, name);
    }

    public static Role ofNew(Roles name) {
        return new Role(null, name);
    }

    public Long getId() {
        return id;
    }

    public Roles getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
