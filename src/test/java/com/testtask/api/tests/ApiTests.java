package com.testtask.api.tests;

import com.testtask.api.contracts.dataObjects.*;
import com.testtask.api.contracts.responses.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//Probably if it was real life automation testing solution, I would organize contracts by endpoints and HTTP methods:
//Creating parametrized requests and responses for each of endpoint and method
//Like GetBooksRequest, GetBooksResponse, PostBooksRequest, PostBooksResponse etc.
//Same for Data Objects. But for purposes of test task this isn't really important, I guess?
//Also, after writing so long on C# I was much more focused on getting this code actually working :)
public class ApiTests {

    private static RequestSpecification spec;

    @BeforeClass
    public void initSpec(){
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://demoqa.com")
                .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
                .addFilter(new RequestLoggingFilter())
                .build();
    }

    @Test
    public void givenUserWantsToAddBookToCollection_WhenUserAddsTheBook_ThenItShouldAppearInCollection() {
        String userName = "testUser";
        String password = "qWEr1@#4";
        String userId;
        String userToken;

        UserDTO user = new UserDTO(userName, password);

        var userResult =
                given()
                        .spec(spec)
                        .body(user)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .statusCode(200)
                        .extract().as(LoginResponse.class);

        assertThat("User ID", userResult.userId, not(is(emptyOrNullString())));
        assertThat("Token", userResult.token, not(is(emptyOrNullString())));
        assertThat("User name", userResult.username, is(userName));

        userId = userResult.userId;
        userToken = userResult.token;

        var booksList =
                given()
                        .spec(spec)
                        .when()
                        .get("/BookStore/v1/Books")
                        .then()
                        .statusCode(200)
                        .extract().as(BooksResponse.class);

        assertThat("Books list not empty",booksList.books.size()>0);

        Random rand = new Random();
        BookDTO randomBook = booksList.books.get(rand.nextInt(booksList.books.size()));

        AddListOfBooksDTO addListOfBooksDTO = new AddListOfBooksDTO(userId, randomBook.isbn);

        //just to clean the collection
        var deleteAllBooks =
                given()
                        .spec(spec)
                        .header("Authorization", "Bearer " + userToken)
                        .when()
                        .delete("/BookStore/v1/Books?UserId=" + userId)
                        .then()
                        .statusCode(204);

        var postBooksResponse =
                given()
                        .spec(spec)
                        .body(addListOfBooksDTO)
                        .header("Authorization", "Bearer " + userToken)
                        .when()
                        .post("/BookStore/v1/Books/")
                        .then()
                        .statusCode(201)
                        .extract().as(PostBooksResponse.class);


        assertThat("Randomly selected book is present in collection",
                    postBooksResponse.books.stream().filter(o -> o.isbn.equals(randomBook.isbn)).findFirst().isPresent());
    }
}
