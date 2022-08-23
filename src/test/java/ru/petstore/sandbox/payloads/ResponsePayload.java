package ru.petstore.sandbox.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponsePayload {
    @JsonProperty
    private int code;
    @JsonProperty
    private String type;
    @JsonProperty
    private String message;

    // default constructor required by Jackson
    public ResponsePayload() {
    }

    public ResponsePayload(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
