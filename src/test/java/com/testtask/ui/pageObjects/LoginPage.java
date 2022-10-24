package com.testtask.ui.pageObjects;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class LoginPage {
    private SelenideElement userNameInput = $(By.id("userName"));
    private SelenideElement passwordInput = $(By.id("password"));
    private SelenideElement loginButton = $(By.id("login"));
    public String url = "https://demoqa.com/login";
    public LoginPage open()
    {
        Selenide.open(this.url);
        webdriver().shouldHave(url(this.url));
        return this;
    }

    public ProfilePage login(String userName, String password)
    {
        userNameInput.setValue(userName);
        passwordInput.setValue(password);
        loginButton.click();

        ProfilePage profilePage = new ProfilePage();
        webdriver().shouldHave(url(profilePage.url));
        return profilePage;
    }
}