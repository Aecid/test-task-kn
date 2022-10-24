package com.testtask.ui.application;

import com.testtask.api.contracts.dataObjects.BookDTO;
import com.testtask.api.contracts.dataObjects.UserDTO;
import com.testtask.api.contracts.responses.LoginResponse;
import com.testtask.api.contracts.responses.UserResponse;
import com.testtask.ui.pageObjects.BooksPage;
import com.testtask.ui.pageObjects.LoginPage;
import com.testtask.ui.pageObjects.ProfilePage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Application {
    private final RequestSpecification spec;

    public Application() {
        this.spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://demoqa.com")
                .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
                .addFilter(new RequestLoggingFilter())
                .build();
    }

    public String userName = "testUser";
    public String password = "qWEr1@#4";
    public String bookName = "Speaking JavaScript";

    public LoginPage loginPage() {
        return new LoginPage();
    }

    public ProfilePage profilePage() {
        return new ProfilePage();
    }

    public BooksPage booksPage() {
        return new BooksPage();
    }

    public void removeAllBooks(String userName, String password) {
        var user = login(userName, password);

        given()
                .spec(spec)
                .header("Authorization", "Bearer " + user.token)
                .when()
                .delete("/BookStore/v1/Books?UserId=" + user.userId)
                .then()
                .statusCode(204);
    }

    public void removeAllBooks() {
        removeAllBooks(this.userName, this.password);
    }

    public LoginResponse login(String userName, String password) {
        UserDTO user = new UserDTO(userName, password);
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().as(LoginResponse.class);
    }

    public LoginResponse login() {
        return login(this.userName, this.password);
    }


    public List<String> getAllBooksInCollection(String userName, String password) {
        // Request URL: https://demoqa.com/Account/v1/User/b0f2ebc4-aae5-491e-afc0-cbfec47cad49
        var user = login(userName, password);
        var allBooks = given()
                .spec(spec)
                .header("Authorization", "Bearer " + user.token)
                .when()
                .get("/Account/v1/User/" + user.userId)
                .then()
                .statusCode(200)
                .extract().as(UserResponse.class);

        List<String> result = new ArrayList<>();
        for (BookDTO book : allBooks.books) {
            result.add(book.title);
        }

        return result;
    }

    public List<String> getAllBooksInCollection() {
        return getAllBooksInCollection(this.userName, this.password);
    }
}
