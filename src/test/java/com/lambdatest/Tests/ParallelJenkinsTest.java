package com.lambdatest.Tests;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ParallelJenkinsTest {

	//public WebDriver driver;
	public static String status = "failed";
	public String buildTag = System.getenv("LT_BUILD");
	//public String username = System.getenv("LT_USERNAME");
	//public String accesskey = System.getenv("LT_APIKEY");
	public String gridURL = System.getenv("LT_GRID_URL");

	@Test(dataProvider = "browsersDetails")
	public void test(String param, Method method) throws Exception {

		// Launch the app
		
		String[] envDeatails = param.split(",");
		String os = envDeatails[1];
		String version = envDeatails[2];
		String browser = envDeatails[0];
		String resValue = envDeatails[3];
		
		this.setUp(browser, version, os, resValue, method.getName());
		
		getWebDriver().get("https://4dvanceboy.github.io/lambdatest/lambdasampleapp.html");

		// Click on First Item
		getWebDriver().findElement(By.name("li1")).click();

		// Click on Second Item
		getWebDriver().findElement(By.name("li2")).click();

		// Add new item is list
		getWebDriver().findElement(By.id("sampletodotext")).clear();
		getWebDriver().findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
		getWebDriver().findElement(By.id("addbutton")).click();

		// Verify Added item
		String item = getWebDriver().findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
		Assert.assertTrue(item.contains("Yey, Let's add it to list"));
		status = "passed";

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		((JavascriptExecutor) webDriver.get()).executeScript("lambda-status="
		+ (result.isSuccess() ? "passed" : "failed"));
		webDriver.get().quit();
	}
	
	
	
	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	@DataProvider(name = "browsersDetails", parallel = true)
	public static Iterator<Object[]> ltBrowserDataProvider(Method testMethod) {
		String jsonText = System.getenv("LT_BROWSERS");

		ArrayList<Object> lt_browser = new ArrayList<Object>();
		ArrayList<Object> lt_operating_system = new ArrayList<Object>();
		ArrayList<Object> lt_browserVersion = new ArrayList<Object>();
		ArrayList<Object> lt_resolution = new ArrayList<Object>();

		JSONArray allData = new JSONArray(jsonText);
		for (int j = 0; j < allData.length(); j++) {
			JSONObject browsersObject = allData.getJSONObject(j);

			if (!browsersObject.getString("browserName").isEmpty()) {
				lt_browser.add(browsersObject.getString("browserName"));
			}

			if (!browsersObject.getString("operatingSystem").isEmpty()) {
				lt_operating_system.add(browsersObject.getString("operatingSystem"));
			}

			if (!browsersObject.getString("browserVersion").isEmpty()) {
				lt_browserVersion.add(browsersObject.getString("browserVersion"));
			}

			if (!browsersObject.getString("resolution").isEmpty()) {
				lt_resolution.add(browsersObject.getString("resolution"));
			}
		}
		Object[][] arrMulti = new Object[lt_browser.size()][1];

		for (int l = 0; l < lt_browser.size(); l++) {

			arrMulti[l][0] = lt_browser.get(l) + "," + lt_operating_system.get(l) + "," + lt_browserVersion.get(l) + ","
					+ lt_resolution.get(l);

		}

		List<Object[]> capabilitiesData = new ArrayList<Object[]>();

		// Object[][] data = new Object[arrMulti.length][1];

		for (int i = 0; i < arrMulti.length; i++) {
			for (int j = 0; j < 1; j++) {

				capabilitiesData.add(new Object[] { arrMulti[i][j] });

			}
		}
		return capabilitiesData.iterator();

	}

	public WebDriver getWebDriver() {
		return webDriver.get();
	}
	
	protected void setUp(String browser, String version, String os, String resolution, String methodName)
			throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// set desired capabilities to launch appropriate browser on Lambdatest
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, version);
		capabilities.setCapability(CapabilityType.PLATFORM, os);
		capabilities.setCapability("name", methodName);
		capabilities.setCapability("build", buildTag);
		capabilities.setCapability("name", "TestNG Parallel");
		capabilities.setCapability("screen_resolution", resolution);
		capabilities.setCapability("network", true);
		capabilities.setCapability("video", true);
		capabilities.setCapability("console", true);
		capabilities.setCapability("visual", true);
		
		//String username = Configuration.readConfig("LambdaTest_UserName");
		//String accesskey = Configuration.readConfig("LambdaTest_AppKey");

		
		// Launch remote browser and set it as the current thread
		//String gridURL = "https://" + username + ":" + accesskey + "@beta-hub.lambdatest.com/wd/hub";
		webDriver.set(new RemoteWebDriver(new URL(gridURL), capabilities));

	}

}