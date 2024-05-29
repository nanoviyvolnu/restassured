package tests;

import Models.AuthRequest;
import Models.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestTestAccount {

    @Test
    public void restTest(){
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("constantinbalaban");
        authRequest.setPassword("PeggdSga123!");
        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(authRequest)
                .when()
                    .post("https://demoqa.com/Account/v1/Authorized")
                .then()
                    .statusCode(200)
                    .extract().response();

        boolean contentType = Boolean.parseBoolean(response.contentType());
        Assert.assertFalse(contentType, "Response for successful authorization can be true!");
    }

    @Test
    public void generateTokenTest(){
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("constantinbalaban");
        authRequest.setPassword("PeggdSga123!");

        String token = RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(authRequest)
                    .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response().jsonPath().getString("token");

        System.out.println(token);
    }

    @Test
    public void getUserTest(){
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("constantinbalaban");
        authRequest.setPassword("PeggdSga123!");

        String token = RestAssured
                .given()
                .log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(authRequest)
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().jsonPath().getString("token");

        AuthResponse authResponse =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(authRequest)
                .when()
                    .post("https://demoqa.com/Account/v1/User")
                .then()
                    .extract().response().getBody().as(AuthResponse.class);

        Assert.assertEquals(authResponse.getMessage(), "User exists!");
    }

}
