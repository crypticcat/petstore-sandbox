package ru.petstore.sandbox.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.petstore.sandbox.payloads.User;

import static io.restassured.RestAssured.given;

public class UserApi extends ApiBase {
    private static ContentType contentTypeJson = ContentType.JSON;
    private static ContentType contentTypeXml = ContentType.XML;
    private static final String CREATE_USER_WITH_LIST = BASE_URL + USER_RESOURCE + CREATE_WITH_LIST;
    private static final String CREATE_USER_WITH_ARRAY = BASE_URL + USER_RESOURCE + CREATE_WITH_ARRAY;
    private static final String LOGIN_USER = BASE_URL + USER_RESOURCE + LOGIN;
    private static final String LOGOUT_USER = BASE_URL + USER_RESOURCE + LOGOUT;

    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";

    /**
     * Method logs user into the system after providing username and password
     *
     * @param username
     * @param password
     * @return
     */
    public static Response loginMasterUser(String username, String password) { //
        return given()
                .contentType(contentTypeJson)
                .with()
                .params(USERNAME_PARAM, username, PASSWORD_PARAM, password)
                .get(LOGIN_USER);
    }

    /**
     * Method creates user based on the provided user data. Can be done only by the logged in user.
     *
     * @param payload - user data
     * @return
     */
    public static Response createUser(User payload) {
        return given()
                .contentType(contentTypeJson)
                .body(payload)
                .when()
                .post(BASE_URL + USER_ENDPOINT);
    }

    //TODO - pay attention how to provide the user data (list, array etc)

    /**
     * Create list of users with the given input array. Logging in is not required
     *
     * @param payload
     * @return
     */
    public static Response createWithArray(User payload) {
        return given()
                .contentType(contentTypeJson)
                .body(payload)
                .when()
                .post(CREATE_USER_WITH_LIST);
    }

    //TODO - pay attention how to provide the user data (list, array etc)

    /**
     * Create list of users with the given input array. Logging in is not required
     *
     * @param payload
     * @return
     */
    public static Response createWithList(User payload) {
        return given()
                .contentType(contentTypeJson)
                .body(payload)
                .when()
                .post(CREATE_USER_WITH_ARRAY);
    }

    /**
     * Method fetches user data by his username. Logging in is not required
     *
     * @param username
     * @return
     */
    public static Response getUserByUsername(String username) { //
        return given().get(BASE_URL + USER_RESOURCE + username);
    }

    /**
     * Method updates user by his username. Can be done only by the logged in user.
     *
     * @param payload  - user data
     * @param username
     * @return
     */
    public static Response updateUserByUsername(User payload, String username) { //
        return given()
                .contentType(contentTypeJson)
                .body(payload)
                .when()
                .put(BASE_URL + USER_RESOURCE + username);
    }

    /**
     * Method deletes user by his username. Can be done only by the logged in user.
     *
     * @param username
     * @return
     */
    public static Response deleteUserByUsername(String username) { //
        return given().delete(BASE_URL + USER_RESOURCE + username);
    }

    /**
     * Method terminates the session of the currently logged in user. No credentials required
     *
     * @return
     */
    public static Response logoutMasterUser() { //
        return given()
                .contentType(contentTypeJson)
                .get(LOGOUT_USER);
    }
}