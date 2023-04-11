package com.wimix.automation.rest;

import com.wimix.automation.rest.dto.AppUrl;
import com.wimix.automation.rest.dto.UploadedApp;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.wimix.automation.core.configuration.SentryConfig.*;
import static io.restassured.RestAssured.given;

public class BrowserStackClient {

    private final String sessionId;

    public BrowserStackClient(String sessionId) {
        this.sessionId = sessionId;
    }

    private final static PreemptiveBasicAuthScheme basicAuth = new PreemptiveBasicAuthScheme();

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(getBsUrl())
            .setAuth(basicAuth)
            .build();

    static {
        basicAuth.setUserName(getBsUserName());
        basicAuth.setPassword(getBsAccessKey());
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public static String uploadApp() {
        return given()
                .spec(requestSpec)
                .contentType(ContentType.MULTIPART)
                .multiPart("file", new File("src/main/resources/app.apk"))
                .post("/app-automate/upload")
                .then()
                .statusCode(200).extract().as(AppUrl.class).getAppUrl();
    }

    public static List<UploadedApp> getListUploadedApps() {
        return Arrays.asList(given()
                .spec(requestSpec)
                .get("/app-automate/recent_apps")
                .then()
                .statusCode(200).extract().as(UploadedApp[].class));
    }

    public static void deleteUploadedApk(String appUrl) {
        given()
                .spec(requestSpec)
                .delete("/app-automate/app/delete/" + appUrl.replaceAll("bs://", ""))
                .then()
                .statusCode(200);
    }

    public static void getDeviceList() {
        given()
                .spec(requestSpec)
                .get("/app-automate/devices.json")
                .then()
                .statusCode(200);
    }

    public static void getSessionList(String buildId) {
        given()
                .spec(requestSpec)
                .get("/app-automate/builds/" + buildId + "/sessions.json")
                .then()
                .statusCode(200);
    }

    public static ExtractableResponse<Response> getSessionDetails(String sessionId) {
        return given()
                .spec(requestSpec)
                .get("/app-automate/sessions/" + sessionId + ".json")
                .then()
                .statusCode(200).extract();
    }

    public void updateSession(String body) {
        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(body)
                .put("/app-automate/sessions/" + sessionId + ".json")
                .then()
                .statusCode(200);
    }
}