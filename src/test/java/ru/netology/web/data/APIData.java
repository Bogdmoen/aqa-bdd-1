package ru.netology.web.data;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class APIData {

    private static String authToken;
    static Response response = null;

    static JsonObject authInfo = new JsonObject(); {
         authInfo.addProperty("login", "vasya");
         authInfo.addProperty("code", "12345");
    }



    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost:9999")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void auth() {
        authToken = RestAssured.given()
                .spec(requestSpec)
                .body(authInfo.toString())
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token")
                .toString();

    }

    public static void getCardInfo() {
        auth();
        response = RestAssured.given()
                .spec(requestSpec)
                .auth()
                .oauth2(authToken)
                .get("/api/cards")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<String> jsonResponse = response.jsonPath().getList("balance");
        System.out.println(jsonResponse);

    }

    @Test
    public void getToken() {
        auth();
        System.out.println(authToken);
    }

    @Test
    public void getCard() {
        getCardInfo();

    }
}
