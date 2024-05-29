package tests;

import Models.Books;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.print.Book;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestTestBooks {

    @Test
    private void getBookListTest(){
        List<Books> booksList =
                given()
                        .log().all()
                .when()
                        .contentType(ContentType.JSON).
                        get("https://demoqa.com/BookStore/v1/books")
                .then()
                        .log().all().extract().body()
                        .jsonPath().getList("books", Books.class);

        booksList.forEach(x-> System.out.println(x.getTitle()));

        Assert.assertFalse(booksList.isEmpty(), "Books list is empty");
    }

//    @Test
//    private void postBookInTheList(){
//
//    }
}
