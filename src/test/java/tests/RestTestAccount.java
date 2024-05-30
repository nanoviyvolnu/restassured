package tests;

import Models.CreateUserRequest;
import Models.CreateUserResponse;
import Models.GetUserResponse;
import Specifications.Specs;
import Utils.GenerateValidPassword;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class RestTestAccount {
    @Test(testName = "Test api for create a new user")
    public void createNewUserTest(){
        Faker faker = new Faker();
        GenerateValidPassword generateValidPassword = new GenerateValidPassword();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserName(faker.name().username());
        createUserRequest.setPassword(generateValidPassword.generatePassword(faker));

        System.out.println(createUserRequest.getUserName());
        System.out.println(createUserRequest.getPassword());

        CreateUserResponse createUserResponse = given()
                .log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post("https://demoqa.com/Account/v1/User")
                .then()
                .log().all().statusCode(201)
                .extract().response().getBody().as(CreateUserResponse.class);

        System.out.println("UserID: " + createUserResponse.getUserID());

        Assert.assertNotNull(createUserResponse.getUserID());
    }

    @Test(testName = "Test api for authorized")
    public void authorizedTest(){
        Faker faker = new Faker();
        GenerateValidPassword generateValidPassword = new GenerateValidPassword();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserName(faker.name().username());
        createUserRequest.setPassword(generateValidPassword.generatePassword(faker));
//        createUserRequest.setUserName("reynaldo.harvey");
//        createUserRequest.setPassword("Vl9)<!)!");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .when()
                .post("https://demoqa.com/Account/v1/Authorized")
                .then()
                .statusCode(200)
                .extract().response();

        boolean contentType = Boolean.parseBoolean(response.contentType());
        Assert.assertFalse(contentType, "Response for successful authorization can be true!");
    }

    @Test(testName = "Test api for generate a token")
    public void generateTokenTest(){
        Faker faker = new Faker();
        GenerateValidPassword generateValidPassword = new GenerateValidPassword();
        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setUserName(faker.name().username());
//        createUserRequest.setPassword(generateValidPassword.generatePassword(faker));
        createUserRequest.setUserName("reynaldo.harvey");
        createUserRequest.setPassword("Vl9)<!)!");

        CreateUserResponse createUserResponse = RestAssured
                .given()
                .log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(CreateUserResponse.class);

        System.out.println(createUserResponse.getToken());
    }

    @Test(testName = "Test api for get user data")
    public void getUserTestById(){
        Specs.installSpec(Specs.requestSpecification("https://demoqa.com/", "Account/v1/"), Specs.responseSpecification());

        Faker faker = new Faker();
        GenerateValidPassword generateValidPassword = new GenerateValidPassword();

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserName(faker.name().username());
        createUserRequest.setPassword(generateValidPassword.generatePassword(faker));

        CreateUserResponse createUserResponse =
                given()
                .when()
                    .body(createUserRequest)
                    .post("User")
                .then()
                    .statusCode(201)
                    .extract().response().as(CreateUserResponse.class);

        System.out.println("UserID: " + createUserResponse.getUserID());

        CreateUserResponse createUserResponse1 = RestAssured
                .given()
                .when()
                    .body(createUserRequest)
                    .post("GenerateToken")
                .then()
                    .statusCode(200)
                    .extract().response().as(CreateUserResponse.class);

        GetUserResponse getUserResponse =
                given()
                    .header("Authorization", "Bearer " + createUserResponse1.getToken())
                    .body(createUserRequest)
                .when()
                    .get("User/" + createUserResponse.getUserID())
                .then()
                    .extract().response().getBody().as(GetUserResponse.class);

        Assert.assertNotNull(createUserResponse1.getToken());
        Assert.assertNotNull(createUserResponse.getUserID());
        Assert.assertEquals(createUserResponse.getUserID(), getUserResponse.getUserId());
    }

}
