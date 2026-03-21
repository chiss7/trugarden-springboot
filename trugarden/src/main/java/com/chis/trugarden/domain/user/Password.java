package com.chis.trugarden.domain.user;

public final class Password {
    private final String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    public static Password ofHashed(String hashedValue) {
        return new Password(hashedValue);
    }

    public String hashedValue() {
        return hashedValue;
    }

    @Override
    public String toString() {
        return "[hashed password]";
    }
}
