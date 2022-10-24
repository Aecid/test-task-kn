package com.testtask.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.testtask.ui.application.Application;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

//This is actually how would I do automation testing on that site.
//Doing UI actions and asserting results with API calls, to focus testing on these UI actions.
//This approach allows to create much faster and more reliable automation suites.
public class UITests {
    Application app = new Application();

    @BeforeClass
    public void setUpAll() {
        Configuration.browserSize = "1920x1080";
        SelenideLogger.addListener("allure", new AllureSelenide());

        //clearing userCollection through API
        app.removeAllBooks();

        //also it is possible to login through API and save cookies for further use. Didn't want to overcomplicate.
        app.loginPage()
                .open()
                .login(app.userName, app.password);
    }

    @BeforeMethod
    public void setUp() {
        }

    //Method naming is a question of code convention inside team.
    @Test
    public void GivenUserWantsToAddBookToCollection_WhenUserClickAddButton_ThenBookShouldBeAdded() {
        //UI part
        app.booksPage()
                .open()
                .selectBookByName(app.bookName)
                .addCurrentBookToYourCollection();

        //API call.
        List<String> allBooksInCollection =
                app.getAllBooksInCollection();

        assertTrue(allBooksInCollection.contains(app.bookName));
    }

    @Test(dependsOnMethods = "GivenUserWantsToAddBookToCollection_WhenUserClickAddButton_ThenBookShouldBeAdded")
    public void GivenUserWantsToRemoveBookFromCollection_WhenUserClicksDeleteBook_ThenBookShouldBeDeleted() {

        //UI part
        app.profilePage()
                .open()
                .deleteBookByName(app.bookName);

        //API call
        List<String> allBooksInCollection =
                app.getAllBooksInCollection();

        assertFalse(allBooksInCollection.contains(app.bookName));
    }
}
