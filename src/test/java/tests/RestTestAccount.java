package tests;

import Models.CreateUserRequest;
import Models.CreateUserResponse;
import Models.GetUserResponse;
import Service.DataInitializer;
import Specifications.Specs;
import Utils.GenerateValidPassword;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestTestAccount {

    private DataInitializer dataInitializer;

    private CreateUserResponse createUserResponse;

    @BeforeTest
    public void setUp(){
        Specs.installSpec(Specs.requestSpecification("https://demoqa.com/", "Account/v1/"), Specs.responseSpecification());
    }


    @Test
    public void setUpCreateUser() {
        dataInitializer = new DataInitializer();
        dataInitializer.initializeDefaultData();

        CreateUserRequest createUserRequest = dataInitializer.getCreateUserRequest();

        createUserResponse = given()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post("https://demoqa.com/Account/v1/User")
                .then()
                .extract().response().as(CreateUserResponse.class);

        if ("User exists!".equals(createUserResponse.getMessage())) {
            System.out.println("!!! User already exists !!!");
            System.out.println("!!!  Create a new user  !!!");
            dataInitializer.initialize();
        }
    }

    @Test
    public void setUpDefaultUser() {
        dataInitializer = new DataInitializer();
        dataInitializer.initializeDefaultData();
    }


    @Test(groups = "createUser", testName = "Test API for creating a new user", dependsOnMethods = "setUpCreateUser")
    public void createNewUserTest() {
        CreateUserRequest createUserRequest = dataInitializer.getCreateUserRequest();

        CreateUserResponse createUserResponse = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(createUserRequest)
                    .post("User")
                .then()
                    .statusCode(201)
                    .extract().response().as(CreateUserResponse.class);

        Assert.assertNotNull(createUserResponse.getUserID());
    }

    @Test(groups = "setUpDefaultUser", testName = "Test API for generating a token", dependsOnMethods = "setUpDefaultUser")
    public void generateTokenTest(){
        CreateUserRequest createUserRequest = dataInitializer.getCreateUserRequest();

        CreateUserResponse createUserResponse = RestAssured
                .given()
                    .when()
                    .body(createUserRequest)
                    .post("GenerateToken")
                .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .as(CreateUserResponse.class);

        Assert.assertNotNull(createUserResponse.getToken());
    }

    @Test(groups = "setUpDefaultUser", testName = "Test api for authorized", dependsOnMethods = "setUpDefaultUser")
    public void authorizedTest(){
        CreateUserRequest createUserRequest = dataInitializer.getCreateUserRequest();

        Response response = given()
                .when()
                    .body(createUserRequest)
                    .post("Authorized")
                .then()
                    .statusCode(200)
                    .extract()
                    .response();

        boolean contentType = Boolean.parseBoolean(response.contentType());
        Assert.assertFalse(contentType, "Response for successful authorization can be true!");
    }


    @Test(groups = "createUser", testName = "Test api for get user data", dependsOnMethods = "setUpCreateUser")
    public void getUserTestById(){
        CreateUserRequest createUserRequest = dataInitializer.getCreateUserRequest();

        CreateUserResponse createUserResponse = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(createUserRequest)
                    .post("User")
                .then()
                    .statusCode(201)
                    .extract()
                    .response()
                    .as(CreateUserResponse.class);

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
