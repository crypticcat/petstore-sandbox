package ru.petstore.sandbox.utils.enums;

public enum Header {

    X_EXPIRES_AFTER("X-Expires-After", ""),
    X_RATE_LIMIT("X-Rate-Limit", Integer.toString(5000)),
    CONTENT_TYPE("Content-Type", "application/json");

    public final String name;
    public final String value;


    Header(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
