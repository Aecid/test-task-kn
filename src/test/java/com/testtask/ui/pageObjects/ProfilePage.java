package com.testtask.ui.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class ProfilePage {
    private SelenideElement deleteAllBooksButton = $x("//button[text()='Delete All Books']");
    private SelenideElement logOutButton = $x("//button[text()='Log out']");
    private SelenideElement modalAcceptButton = $("#closeSmallModal-ok");
    private SelenideElement modalWindow = $("div.modal-content");
    private SelenideElement userLabel = $("#userName-label");

    public String url = "https://demoqa.com/profile";

    //in working conditions if there are ads on site I'd use something like custom chrome profile to disable ads.

    public ProfilePage open() {
        Selenide.open(this.url);
        webdriver().shouldHave(url(this.url));
        return this;
    }

    //Generally it is possible to iterate through available pages, but there is no sense in such simple check.
    public List<String> getAllBooksTitlesInCollection() {
        List<String> result = new ArrayList<>();
        List<SelenideElement> allBookAnchorElements = $$x("//span[@class=\"mr-2\"]/a");
        for (SelenideElement bookAnchor : allBookAnchorElements)
        {
            result.add(bookAnchor.innerText());
        }

        return result;
    }

    public ProfilePage deleteBookByName(String bookName) {
        if (getAllBooksTitlesInCollection().contains(bookName))
        {
            SelenideElement deleteButton = $x("//span[@id='see-book-" + bookName + "']/../../..//span[@id='delete-record-undefined']");
            deleteButton.click();
            modalAcceptButton.shouldBe(Condition.visible).click();
            modalWindow.shouldBe(Condition.disappear);
            refresh(); //kinda unnecessary. But there is an alert that behaves chaotically, so instead of catching it, I'll just refresh the page.
        }

        return this;
    }

    public ProfilePage deleteAllBooksIfAny() {
        if (getAllBooksTitlesInCollection().size() > 0) {
            deleteAllBooksButton.scrollIntoView(true).click();
            modalAcceptButton.shouldBe(Condition.visible).click();
            modalWindow.shouldBe(Condition.disappear);
            refresh();
            //switchTo().alert().accept();
        }

        return this;
    }

    public LoginPage logOut()
    {
        logOutButton.click();
        LoginPage loginPage = new LoginPage();
        webdriver().shouldHave(url(loginPage.url));
        return loginPage;
    }
}