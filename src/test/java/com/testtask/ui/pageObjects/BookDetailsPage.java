package com.testtask.ui.pageObjects;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

// page_url = about:blank
public class BookDetailsPage {
    private SelenideElement addBookToCollectionButton = $x("//button[text()='Add To Your Collection']");

    public BookDetailsPage addCurrentBookToYourCollection()
    {
        addBookToCollectionButton.scrollIntoView(true).click();
//        try {
            Selenide.switchTo().alert().dismiss();
//        }
//        catch (TimeoutException ex) { }

        return this;
    }
}