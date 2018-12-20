package com.lambdatest.Tests;

import java.net.MalformedURLException;
import java.net.URL;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.lambdatest.Pages.ToDo;

public class LocalTest {
	
public  static WebDriver driver;
	
	public static String username = System.getenv("LT_USERNAME");
	public static String accesskey = System.getenv("LT_APPKEY");
    public static String gridURL = System.getenv("LT_GRID_URL");
    public static String os = System.getenv("LT_OPERATING_SYSTEM");
    public static String browser = System.getenv("LT_BROWSER_NAME");
    public static String version = System.getenv("LT_BROWSER_VERSION");
    public static String res = System.getenv("LT_RESOLUTION");
	
	@BeforeClass
	public void setUp() throws MalformedURLException {
		
		if (browser==null)
			browser= "chrome";
		
		if (version==null)
			version= "64.0";
		
		if (os==null)
			os= "WIN10";
		
		if (res==null)
			res= "1024x768";
		
		if (username==null)
			// Set you LAMBDATEST Username here if not Provided from Jenkins
        	username = "nikhily";
        
        if (accesskey==null) 
        	// Set you LAMBDATEST AppKey here if not Provided from Jenkins
        	accesskey = "3Z6PQHL1sUEW2nuvvPZxg7zom6y8ZqAvfPJBX5Ne2rWN3uwfGi";
        
        DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(CapabilityType.BROWSER_NAME, browser);
		capability.setCapability(CapabilityType.VERSION, version);
		capability.setCapability(CapabilityType.PLATFORM, os);
		capability.setCapability("build", "TestNG Local Test");
		capability.setCapability("screen_resolution", res);
		capability.setCapability("tunnel", true);
		capability.setCapability("tunnelIdentifier", "Tunnel_name");
		capability.setCapability("network", true);
		capability.setCapability("video", true);
		capability.setCapability("console", true);
		capability.setCapability("visual", true);
        
        if (gridURL==null)
        	gridURL = "https://" + username + ":" + accesskey + "@stage-hub.lambdatest.com/wd/hub";
        
        
		driver = new RemoteWebDriver(new URL(gridURL),capability);
	}
	
	@Test
	public void test() {
		 
		//Launch the app
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
	
	
	@AfterClass
	public void afterTest()
	{
		//((JavascriptExecutor) driver).executeScript("lambda-passed=" + status);
		driver.quit();
	}
	

}