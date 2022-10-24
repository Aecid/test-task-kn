package com.testtask.ui.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

// page_url = https://www.jetbrains.com/
public class BooksPage {
    private List<SelenideElement> getAllBooksAnchors()
    {
      return $$x("//span[@class=\"mr-2\"]/a");
    }

    public String url = "https://demoqa.com/books";
    public BooksPage open()
    {
        Selenide.open(this.url);
        webdriver().shouldHave(url(this.url));
        return this;
    }

    public List<String> getAllBookTitles()
    {
        List<String> result = null;
        for (SelenideElement bookElement : getAllBooksAnchors())
        {
            result.add(bookElement.innerText());
        }

        return result;
    }

    public BookDetailsPage selectBookByName(String bookName)
    {
        SelenideElement bookAnchor = $x("//span[@id='see-book-"+bookName+"']/a").shouldBe(Condition.visible);
        bookAnchor.click();
        return new BookDetailsPage();
    }
}
