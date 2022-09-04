package ru.petstore.sandbox.tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import ru.petstore.sandbox.payloads.ResponsePayload;
import ru.petstore.sandbox.payloads.User;
import ru.petstore.sandbox.payloads.UserResponse;
import ru.petstore.sandbox.requests.UserApi;
import ru.petstore.sandbox.utils.UtilBase;

import static ru.petstore.sandbox.utils.enums.Header.*;
import static ru.petstore.sandbox.utils.enums.StatusCode.OK_200;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserApiHappyPathTest extends TestBase {

    private static final String MASTER_USERNAME = random.name().username();
    private static final String MASTER_PASSWORD = random.cat().name();
    private static User userData = new User();
    private static int expectedId = userData.getId();
    private static String expectedUsername = userData.getUsername();
    private static String expectedFirstName = userData.getFirstName();
    private static String expectedLastName = userData.getLastName();
    private static String expectedEmail = userData.getEmail();
    private static String expectedPassword = userData.getPassword();
    private static String expectedPhone = userData.getPhone();
    private static int expectedUserStatus = userData.getUserStatus();


    @Test
    @Order(1)
    @Description("Login master user to start a new session")
    public void loginMasterUser() {
        SoftAssertions softAssert = new SoftAssertions();
        int allowedCallsPerHour = Integer.parseInt(X_RATE_LIMIT.value);

        LOG.info("Logging in as a new master user with username: {} and password: {}",
                MASTER_USERNAME, MASTER_PASSWORD);
        Response response = UserApi.loginMasterUser(MASTER_USERNAME, MASTER_PASSWORD);
        String contentType = response.getHeader(CONTENT_TYPE.name);
        String xExpiresAfter = response.getHeader(X_EXPIRES_AFTER.name);
        int xRateLimit = Integer.parseInt(response.getHeader(X_RATE_LIMIT.name));
        String tokenExpireDate = UtilBase.getExpireDateInString("E MMM dd HH:mm",
                "UTC", 1);

        LOG.info("Performing checks for the response code and headers");
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);
        softAssert.assertThat(xExpiresAfter)
                .withFailMessage(String.format("Expected expiration date is: %s, but was %s",
                        tokenExpireDate, xExpiresAfter))
                .contains(tokenExpireDate);
        softAssert.assertThat(xRateLimit)
                .withFailMessage(String.format("Expected calls per hour: %s, but was %s",
                        allowedCallsPerHour, xRateLimit))
                .isEqualTo(allowedCallsPerHour);

        LOG.info("Performing checks for the response body");
        ResponsePayload userResponse = response.as(ResponsePayload.class);
        softAssert.assertThat(userResponse.getCode())
                .withFailMessage(String.format("Expected code in response is: %s, but was %s",
                        OK_200.code, userResponse.getCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(userResponse.getMessage())
                .withFailMessage("Expected message should contain info about logged in user session," +
                        "but contains %s", userResponse.getMessage())
                .isNotEmpty();

        softAssert.assertAll();
    }

    @Test
    @Order(2)
    @Description("Create new user after the master user is logged in")
    public void createUser() {
        SoftAssertions softAssert = new SoftAssertions();

        LOG.info("Creating new user with username {}", expectedUsername);
        Response response = UserApi.createUser(userData);

        LOG.info("Performing checks for the response code and headers");
        String contentType = response.getHeader(CONTENT_TYPE.name);
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);

        LOG.info("Performing checks for the response body");
        ResponsePayload responsePayload = response.as(ResponsePayload.class);
        int idFromResponse = Integer.parseInt(responsePayload.getMessage());
        softAssert.assertThat(responsePayload.getCode())
                .withFailMessage(String.format("Expected code in response is: %s, but was %s",
                        OK_200.code, responsePayload.getCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(idFromResponse)
                .withFailMessage("Expected message should contain id of created user: %s, " +
                        "but contains: %s", expectedId, idFromResponse)
                .isEqualTo(expectedId);

        softAssert.assertAll();
    }

    @Test
    @Order(3)
    @Description("Get the data of the newly created user")
    public void getUser() {
        SoftAssertions softAssert = new SoftAssertions();

        LOG.info("Retrieving the data for the user with the username {}", expectedUsername);
        Response response = UserApi.getUserByUsername(expectedUsername);

        LOG.info("Performing checks for the response code and headers");
        String contentType = response.getHeader(CONTENT_TYPE.name);
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);

        LOG.info("Performing checks for the response body");
        UserResponse responsePayload = response.as(UserResponse.class);
        int idFromResp = responsePayload.getId();
        softAssert.assertThat(idFromResp)
                .withFailMessage(String.format("Expected id in response is: %s, but was %s",
                        expectedId, idFromResp))
                .isEqualTo(expectedId);

        String usernameFromResp = responsePayload.getUsername();
        softAssert.assertThat(usernameFromResp)
                .withFailMessage("Expected username is: %s, but was: %s",
                        expectedUsername, usernameFromResp)
                .isEqualTo(expectedUsername);

        String firstNameFromResp = responsePayload.getFirstName();
        softAssert.assertThat(firstNameFromResp)
                .withFailMessage("Expected first name is: %s, but was: %s",
                        expectedFirstName, firstNameFromResp)
                .isEqualTo(expectedFirstName);

        String lastNameFromResp = responsePayload.getLastName();
        softAssert.assertThat(lastNameFromResp)
                .withFailMessage("Expected last name is: %s, but was: %s",
                        expectedLastName, lastNameFromResp)
                .isEqualTo(expectedLastName);

        String emailFromResp = responsePayload.getEmail();
        softAssert.assertThat(emailFromResp)
                .withFailMessage("Expected email is: %s, but was: %s",
                        expectedEmail, emailFromResp)
                .isEqualTo(expectedEmail);

        String passFromResp = responsePayload.getPassword();
        softAssert.assertThat(passFromResp)
                .withFailMessage("Expected password is: %s, but was: %s",
                        expectedPassword, passFromResp)
                .isEqualTo(expectedPassword);

        String phoneFromResp = responsePayload.getPhone();
        softAssert.assertThat(phoneFromResp)
                .withFailMessage("Expected phone number is: %s, but was: %s",
                        expectedPhone, phoneFromResp)
                .isEqualTo(expectedPhone);

        int userStatusFromResp = responsePayload.getUserStatus();
        softAssert.assertThat(userStatusFromResp)
                .withFailMessage("Expected user status is: %s, but was: %s",
                        expectedUserStatus, userStatusFromResp)
                .isEqualTo(expectedUserStatus);

        softAssert.assertAll();
    }

    @Test
    @Order(4)
    @Description("Update the newly created user")
    public void updateUser() {
        SoftAssertions softAssert = new SoftAssertions();
        String updatedFirstName = random.name().firstName();
        String updatedLastName = random.name().lastName();

        LOG.debug("Updating the data for created user with username {}", expectedUsername);
        userData.setFirstName(updatedFirstName);
        userData.setLastName(updatedLastName);
        Response response = UserApi.updateUserByUsername(userData, expectedUsername);

        LOG.info("Performing checks for the response code and headers");
        String contentType = response.getHeader(CONTENT_TYPE.name);
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);

        LOG.info("Performing checks for the response body");
        ResponsePayload responsePayload = response.as(ResponsePayload.class);
        int idFromResponse = Integer.parseInt(responsePayload.getMessage());
        softAssert.assertThat(responsePayload.getCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, responsePayload.getCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(idFromResponse)
                .withFailMessage("Expected message should contain id of created user: %s, " +
                        "but contains: %s", expectedId, idFromResponse)
                .isEqualTo(expectedId);

        softAssert.assertAll();
    }

    @Test
    @Order(5)
    @Description("Delete the created and updated user")
    public void deleteUser() {
        SoftAssertions softAssert = new SoftAssertions();

        LOG.debug("Deleting user with the user name {}", expectedUsername);
        Response response = UserApi.deleteUserByUsername(expectedUsername);

        LOG.debug("Performing checks for the response code and headers");
        String contentType = response.getHeader(CONTENT_TYPE.name);
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);

        LOG.debug("Performing checks for the response body");
        ResponsePayload responsePayload = response.as(ResponsePayload.class);
        softAssert.assertThat(responsePayload.getCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, responsePayload.getCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(responsePayload.getMessage())
                .withFailMessage("Expected message should contain the username of deleted user, " +
                        "but contains %s", responsePayload.getMessage())
                .isEqualTo(expectedUsername);

        softAssert.assertAll();
    }

    @Test
    @Order(6)
    @Description("Log out master user to end a session")
    public void logoutMasterUser() {
        SoftAssertions softAssert = new SoftAssertions();

        LOG.info("Logging out master user from the current session");
        Response response = UserApi.logoutMasterUser();

        LOG.info("Performing checks for the response code and headers");
        String contentType = response.getHeader(CONTENT_TYPE.name);
        softAssert.assertThat(response.getStatusCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, response.getStatusCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(contentType)
                .withFailMessage(String.format("Expected Content-Type header is: %s, but was %s",
                        CONTENT_TYPE.value, contentType))
                .isEqualTo(CONTENT_TYPE.value);

        LOG.info("Performing checks for the response body");
        ResponsePayload responsePayload = response.as(ResponsePayload.class);
        softAssert.assertThat(responsePayload.getCode())
                .withFailMessage(String.format("Expected status code is: %s, but was %s",
                        OK_200.code, responsePayload.getCode()))
                .isEqualTo(OK_200.code);
        softAssert.assertThat(responsePayload.getMessage())
                .withFailMessage("Expected message should contain info about logged in user session," +
                        "but contains %s", responsePayload.getMessage())
                .isEqualTo("ok");

        softAssert.assertAll();
    }
}
