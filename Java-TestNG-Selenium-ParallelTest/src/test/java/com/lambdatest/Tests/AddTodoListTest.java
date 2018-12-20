package com.lambdatest.Tests;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lambdatest.Pages.ToDo;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;


public class AddTodoListTest extends TestBase {


    @Test(dataProvider = "browsersDetails")
    public void addTodoListTest(String browser, String version, String os, Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        //create webdriver session
        this.setUp(browser, version, os, method.getName());
        WebDriver driver = this.getWebDriver();

        //Visiting Application  page
        ToDo page = ToDo.visitPage(driver);

        //Click on First Item
        page.clickOnFirstItem();
        
        //Click on Second Item
        page.clickOnSecondItem();
        
        //Add new item is list
        page.addNewItem("Yey, Let's add it to list");
        
        //Verify Added item
        page.verifyAddeItem();
    }
}