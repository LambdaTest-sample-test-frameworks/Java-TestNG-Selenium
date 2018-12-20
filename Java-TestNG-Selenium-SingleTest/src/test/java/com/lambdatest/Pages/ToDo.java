package com.lambdatest.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ToDo {

    @FindBy(name = "li1")
    private WebElement firstItem;

    @FindBy(name = "li2")
    private WebElement secondItem;

    @FindBy(id = "sampletodotext")
    private WebElement textfiled;

    @FindBy(id = "addbutton")
    private WebElement add;
    
    @FindBy(xpath = "/html/body/div/div/div/ul/li[6]/span")
    private WebElement addedItem;

    public WebDriver driver;
    public static String url = "https://4dvanceboy.github.io/lambdatest/lambdasampleapp.html";

    public static ToDo visitPage(WebDriver driver) {
        ToDo page = new ToDo(driver);
        page.visitPage();
        return page;
    }

    public ToDo(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void visitPage() {
        this.driver.get(url);
    }

    public void clickOnFirstItem() {
        firstItem.click();
    }
    
    public void clickOnSecondItem() {
        secondItem.click();
    }

    public void addNewItem(String text) {
    	textfiled.clear();
    	textfiled.sendKeys(text);
        add.click();
    }

    public void verifyAddeItem() {
         String item = addedItem.getText();
         Assert.assertTrue(item.contains("Yey, Let's add it to list"));
    }
}
