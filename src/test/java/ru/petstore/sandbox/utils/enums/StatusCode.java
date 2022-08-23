package ru.petstore.sandbox.utils.enums;

public enum StatusCode {
    OK_200(200),
    CREATED_201(201),
    ACCEPTED_202(202),
    FORBIDDEN_403(403);

    public final int code;

    StatusCode(int code) {
        this.code = code;
    }
}
