package Specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {
    public static RequestSpecification requestSpecification(String baseURL, String basePath){
        return new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification responseSpecification(){
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    public static void installSpec(RequestSpecification requestSpecification, ResponseSpecification responseSpecification){
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }
}
