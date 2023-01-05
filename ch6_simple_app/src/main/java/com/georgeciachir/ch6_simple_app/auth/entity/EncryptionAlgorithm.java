package com.georgeciachir.ch6_simple_app.auth.entity;

public enum EncryptionAlgorithm {

    BCRYPT,
    SCRYPT;

    public String getType() {
        return String.format("{%s}", this.name());
    }
}
