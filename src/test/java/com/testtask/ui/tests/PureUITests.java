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

//Everything is done through Selenium WebDriver. Just to show that I can do it.
//This is kinda only way to create E2E tests purely on WebDriver, but in general I tend to avoid that approach as much as possible
//See UITests class for the approach I would use in real life scenario.
public class PureUITests {
    Application app = new Application();

    @BeforeClass
    public void setUpAll() {
        Configuration.browserSize = "1920x1080";
        SelenideLogger.addListener("allure", new AllureSelenide());

        app.loginPage()
                .open()
                .login(app.userName, app.password);
        app.profilePage()
                .open()
                .deleteAllBooksIfAny();
    }

    @BeforeMethod
    public void setUp() {
        }

    //Method naming is a question of code convention inside team.
    @Test
    public void GivenUserWantsToAddBookToCollection_WhenUserClickAddButton_ThenBookShouldBeAdded() {

        app.booksPage()
                .open()
                .selectBookByName(app.bookName)
                .addCurrentBookToYourCollection();

        List<String> allBooksInCollection =
                app.profilePage()
                        .open()
                        .getAllBooksTitlesInCollection();

        assertTrue(allBooksInCollection.contains(app.bookName));
    }

    @Test(dependsOnMethods = "GivenUserWantsToAddBookToCollection_WhenUserClickAddButton_ThenBookShouldBeAdded")
    public void GivenUserWantsToRemoveBookFromCollection_WhenUserClicksDeleteBook_ThenBookShouldBeDeleted() {

        List<String> allBooksInCollection =
                app.profilePage()
                        .open()
                        .deleteBookByName(app.bookName)
                        .getAllBooksTitlesInCollection();

        assertFalse(allBooksInCollection.contains(app.bookName));
    }
}
