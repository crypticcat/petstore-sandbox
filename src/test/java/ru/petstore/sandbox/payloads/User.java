package ru.petstore.sandbox.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.datafaker.Faker;

public class User {
    protected static final Faker random = new Faker();

    @JsonProperty
    private int id;
    @JsonProperty
    private String username;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    @JsonProperty
    private String phone;
    @JsonProperty
    private int userStatus;

    public User() {
        id = random.number().randomDigitNotZero();
        username = random.name().username();
        firstName = random.name().firstName();
        lastName = random.name().lastName();
        email = random.internet().emailAddress();
        password = random.animal().name();
        phone = random.phoneNumber().cellPhone();
        userStatus = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
